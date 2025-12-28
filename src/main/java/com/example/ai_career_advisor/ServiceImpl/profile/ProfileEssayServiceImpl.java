package com.example.ai_career_advisor.ServiceImpl.profile;

import com.example.ai_career_advisor.Constant.AiScenario;
import com.example.ai_career_advisor.Constant.YnType;
import com.example.ai_career_advisor.DTO.ai.AiEssayRequestDTO;
import com.example.ai_career_advisor.DTO.ai.AiEssayResultDTO;
import com.example.ai_career_advisor.DTO.profile.ai.ProfileEssaySummaryDTO;
import com.example.ai_career_advisor.DTO.profile.view.ProfileDetailViewDTO;
import com.example.ai_career_advisor.Entity.profile.ai.PrfProfileEssay;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import com.example.ai_career_advisor.Repository.profile.ai.PrfProfileEssayRepository;
import com.example.ai_career_advisor.Repository.profile.core.PrfProfileRepository;
import com.example.ai_career_advisor.Service.ai.AiEssayService;
import com.example.ai_career_advisor.Service.profile.ProfileEssayService;
import com.example.ai_career_advisor.Service.profile.ProfileService;
import com.example.ai_career_advisor.Util.ai.AiEssayMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 프로필 자기소개서 버전 관리 & 생성 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileEssayServiceImpl implements ProfileEssayService {

    private final ProfileService profileService;
    private final PrfProfileRepository prfProfileRepository;

    //자기소계서 DB저장용 레퍼 지토리 : 프로필 및 userID 을 따라감
    private final PrfProfileEssayRepository prfProfileEssayRepository;

    //자기소계서 추론 입력값 DTO
    private final AiEssayMapper aiEssayMapper;

    //자기 소계서 AI 추론 : 서비스로직(ESSAY:자기 소계서,CAREER_ANALYSIS
    private final AiEssayService aiEssayService;

    @Override
    @Transactional
    public AiEssayResultDTO generateAndSaveEssay(Long profileId, Long currentUserId) {

        // 1) 프로필 상세 조회 + 접근 권한 검증
        ProfileDetailViewDTO detail = profileService.getProfileDetail(profileId, currentUserId);

        // 2) 프로필 상세 → AI 요청 DTO 변환
        AiEssayRequestDTO requestDTO = aiEssayMapper.fromProfileDetail(detail);

        // 3) AI 서비스 호출 (현재는 Stub)
        AiEssayResultDTO essayResult = aiEssayService.generateEssay(requestDTO);

        // 4) 다음 버전 번호 계산
        int nextVersion = prfProfileEssayRepository
                .findFirstByProfileProfileIdOrderByVersionNoDesc(profileId)
                .map(e -> e.getVersionNo() + 1)
                .orElse(1);

        // 5) 프로필 엔티티 로딩
        PrfProfile profile = prfProfileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로필입니다. profileId=" + profileId));

        // 6) 기존 대표 자소서들(is_main=Y)을 전부 N 으로 변경
        List<PrfProfileEssay> currentMains =
                prfProfileEssayRepository.findByProfileProfileIdAndIsMain(profileId, YnType.Y);

        for (PrfProfileEssay mainEssay : currentMains) {
            mainEssay.setIsMain(YnType.N);
            // JPA 영속 상태이므로 save 호출 없이도 @Transactional 끝나면 자동 flush
        }

        // 7) 새 자소서를 대표(Y)로 저장
        PrfProfileEssay essayEntity = PrfProfileEssay.builder()
                .profile(profile)
                .scenario(AiScenario.ESSAY)
                .versionNo(nextVersion)
                .isMain(YnType.Y)   // 새로 생성되는 버전 = 대표
                .title(detail.getProfileTitle() + " - 자기소개서 v" + nextVersion)
                .content(essayResult.getRawEssayText())
                .modelName("STUB")  // 이후 실제 모델명으로 교체
                .promptKey(AiScenario.ESSAY.getPromptKey())
                .build();

        PrfProfileEssay saved = prfProfileEssayRepository.save(essayEntity);

        log.info("자기소개서 저장 완료. essayId={}, profileId={}, version={}, isMain={}",
                saved.getEssayId(), profileId, nextVersion, saved.getIsMain());

        // 필요 시 essayId, versionNo를 essayResult에 추가하는 확장도 가능
        return essayResult;
    }

    /**
     * 프로필 AI 자소서 생성 : 결과 보여주는 리스트
     * */
    @Override
    @Transactional(readOnly = true)
    public List<ProfileEssaySummaryDTO> getEssaySummaries(Long profileId, Long currentUserId) {

        // 1) 프로필 접근권한 검증을 위해 한 번 조회 (예외 발생 시 자동 차단)
        profileService.getProfileDetail(profileId, currentUserId);

        // 2) 해당 프로필의 자소서 버전 목록 조회
        List<PrfProfileEssay> essays =
                prfProfileEssayRepository.findByProfileProfileIdOrderByVersionNoDesc(profileId);

        // 3) View용 DTO로 변환
        List<ProfileEssaySummaryDTO> summaryList = essays.stream()
                .map(e -> ProfileEssaySummaryDTO.builder()
                        .essayId(e.getEssayId())
                        .versionNo(e.getVersionNo())
                        .main(e.getIsMain() == YnType.Y)
                        .title(e.getTitle())
                        .modelName(e.getModelName())
                        .previewText(buildPreview(e.getContent()))
                        .regDate(e.getRegDate())
                        .build())
                .collect(Collectors.toList());

        return summaryList;
    }



    @Override
    @Transactional(readOnly = true)
    public AiEssayResultDTO getMainEssayResult(Long profileId, Long currentUserId) {

        // 1) 접근권한 검증: 프로필이 현재 사용자 소유인지 확인
        profileService.getProfileDetail(profileId, currentUserId);

        // 2) 대표 자소서 조회 (is_main = Y)
        return prfProfileEssayRepository
                .findFirstByProfileProfileIdAndIsMainOrderByVersionNoDesc(profileId, YnType.Y)
                .map(entity -> {
                    // DB 엔티티 → AiEssayResultDTO 로 변환
                    AiEssayResultDTO dto = AiEssayResultDTO.builder()
                            .scenario(AiScenario.ESSAY)
                            .title(entity.getTitle())
                            .rawEssayText(entity.getContent())
                            .modelName(entity.getModelName())
                            .build();

                    return dto;
                })
                .orElse(null); // 대표 자소서가 아직 없으면 null
    }


    /**
     * 본문에서 앞부분 일부만 잘라서 preview 텍스트를 생성합니다.
     */
    private String buildPreview(String content) {
        if (content == null) {
            return "";
        }
        int maxLen = 80;
        String trimmed = content.replace("\r\n", " ")
                .replace("\n", " ")
                .trim();
        if (trimmed.length() <= maxLen) {
            return trimmed;
        }
        return trimmed.substring(0, maxLen) + "...";
    }








}

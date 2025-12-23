package com.example.ai_career_advisor.ServiceImpl.profile;

import com.example.ai_career_advisor.Constant.ExperienceType;
import com.example.ai_career_advisor.DTO.profile.request.ProfileConcernRequestDTO;
import com.example.ai_career_advisor.DTO.profile.request.ProfileCreateRequestDTO;
import com.example.ai_career_advisor.DTO.profile.request.ProfileExperienceRequestDTO;
import com.example.ai_career_advisor.DTO.profile.view.ProfileDetailViewDTO;
import com.example.ai_career_advisor.Entity.master.MasConcern;
import com.example.ai_career_advisor.Entity.master.MasSkill;
import com.example.ai_career_advisor.Entity.master.MasValue;
import com.example.ai_career_advisor.Entity.master.MasWorkEnv;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import com.example.ai_career_advisor.Entity.profile.experience.PrfProfileExperience;
import com.example.ai_career_advisor.Entity.profile.mapping.*;
import com.example.ai_career_advisor.Entity.user.AppUser;
import com.example.ai_career_advisor.Repository.master.*;
import com.example.ai_career_advisor.Repository.profile.core.PrfProfileRepository;
import com.example.ai_career_advisor.Repository.profile.experience.PrfProfileExperienceRepository;
import com.example.ai_career_advisor.Repository.profile.mapping.*;
import com.example.ai_career_advisor.Service.profile.ProfileService;
import com.example.ai_career_advisor.Service.user.UserService;

import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 프로필 도메인 서비스 구현체
 * - 1차 파이프라인: User 생성/조회 → Profile 생성 → 스킬/경험/가치관/환경/고민 매핑 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserService userService;

    private final PrfProfileRepository prfProfileRepository;
    private final PrfProfileExperienceRepository prfProfileExperienceRepository;

    private final PrfProfileSkillRepository prfProfileSkillRepository;
    private final PrfProfileValueRankRepository prfProfileValueRankRepository;
    private final PrfProfileCareerPriorityRepository prfProfileCareerPriorityRepository;
    private final PrfProfileWorkEnvRepository prfProfileWorkEnvRepository;
    private final PrfProfileConcernRepository prfProfileConcernRepository;

    private final MasSkillRepository masSkillRepository;
    private final MasValueRepository masValueRepository;
    private final MasWorkEnvRepository masWorkEnvRepository;
    private final MasConcernRepository masConcernRepository;

    @Override
    @Transactional
    public PrfProfile createProfileWithDetails(ProfileCreateRequestDTO request) {

        // 1) 사용자 조회 또는 생성
        AppUser user = userService.getOrCreateUser(
                request.getDisplayName(),
                request.getEmail(),
                request.getUserGroup()
        );

        // 2) 프로필 생성 및 저장
        PrfProfile profile = PrfProfile.builder()
                .user(user)
                .profileType(request.getProfileType())
                .profileTitle(request.getProfileTitle())
                .build();

        PrfProfile savedProfile = prfProfileRepository.save(profile);

        // profileId를 미리 변수로 꺼내서 사용
        Long profileId = savedProfile.getProfileId();

        // 3) 경험(1:N) 저장
        List<ProfileExperienceRequestDTO> experienceRequests = request.getExperiences();
        if (experienceRequests != null && !experienceRequests.isEmpty()) {
            for (ProfileExperienceRequestDTO expReq : experienceRequests) {
                PrfProfileExperience experience = PrfProfileExperience.builder()
                        .profile(savedProfile)
                        .experienceType(expReq.getExperienceType())
                        .title(expReq.getTitle())
                        .organization(expReq.getOrganization())
                        .startDate(expReq.getStartDate())
                        .endDate(expReq.getEndDate())
                        .description(expReq.getDescription())
                        .outcome(expReq.getOutcome())
                        .linkUrl(expReq.getLinkUrl())
                        .build();

                PrfProfileExperience savedExperience = prfProfileExperienceRepository.save(experience);
                // savedExperience 변수는 추후 확장(로그/이벤트)에 활용 가능
            }
            log.info("경험 개수 = {}", experienceRequests.size());

        }

        // 4) 스킬 매핑 저장
        List<String> skillCodes = request.getSkillCodes();
        if (skillCodes != null && !skillCodes.isEmpty()) {
            for (String skillCode : skillCodes) {
                MasSkill skill = masSkillRepository.findById(skillCode)
                        .orElseThrow(() -> {
                            String message = "존재하지 않는 스킬 코드입니다. skillCode=" + skillCode;
                            return new IllegalArgumentException(message);
                        });

                PrfProfileSkillId id = new PrfProfileSkillId(profileId, skillCode);

                PrfProfileSkill profileSkill = PrfProfileSkill.builder()
                        .id(id)
                        .profile(savedProfile)
                        .skill(skill)
                        .build();

                PrfProfileSkill savedProfileSkill = prfProfileSkillRepository.save(profileSkill);
            }
        }

        // 5) 가치관 랭킹 저장 (리스트 순서 = rank_order)
        List<String> valueCodesOrdered = request.getValueCodesOrdered();
        if (valueCodesOrdered != null && !valueCodesOrdered.isEmpty()) {
            int rank = 1;
            for (String valueCode : valueCodesOrdered) {
                MasValue value = masValueRepository.findById(valueCode)
                        .orElseThrow(() -> {
                            String message = "존재하지 않는 가치관 코드입니다. valueCode=" + valueCode;
                            return new IllegalArgumentException(message);
                        });

                PrfProfileValueRankId id = new PrfProfileValueRankId(profileId, valueCode);

                PrfProfileValueRank valueRank = PrfProfileValueRank.builder()
                        .id(id)
                        .profile(savedProfile)
                        .value(value)
                        .rankOrder(rank)
                        .build();

                PrfProfileValueRank savedValueRank = prfProfileValueRankRepository.save(valueRank);
                rank++;
            }
        }

        // 6) 진로 후보 우선순위 저장
        List<String> careerOptionCodesOrdered = request.getCareerOptionCodesOrdered();
        if (careerOptionCodesOrdered != null && !careerOptionCodesOrdered.isEmpty()) {
            int priorityOrder = 1;
            for (String careerCode : careerOptionCodesOrdered) {
                PrfProfileCareerPriorityId id = new PrfProfileCareerPriorityId(profileId, careerCode);

                PrfProfileCareerPriority priority = PrfProfileCareerPriority.builder()
                        .id(id)
                        .profile(savedProfile)
                        .priorityOrder(priorityOrder)
                        .build();

                PrfProfileCareerPriority savedPriority =
                        prfProfileCareerPriorityRepository.save(priority);

                priorityOrder++;
            }
        }

        // 7) 근무환경 매핑 저장
        List<String> workEnvCodes = request.getWorkEnvCodes();
        if (workEnvCodes != null && !workEnvCodes.isEmpty()) {
            for (String envCode : workEnvCodes) {
                MasWorkEnv env = masWorkEnvRepository.findById(envCode)
                        .orElseThrow(() -> {
                            String message = "존재하지 않는 근무환경 코드입니다. envCode=" + envCode;
                            return new IllegalArgumentException(message);
                        });

                PrfProfileWorkEnvId id = new PrfProfileWorkEnvId(profileId, envCode);

                PrfProfileWorkEnv workEnv = PrfProfileWorkEnv.builder()
                        .id(id)
                        .profile(savedProfile)
                        .env(env)
                        .build();

                PrfProfileWorkEnv savedWorkEnv = prfProfileWorkEnvRepository.save(workEnv);
            }
        }

        // 8) 고민 매핑 저장
        List<ProfileConcernRequestDTO> concernRequests = request.getConcerns();

        //로그 찍어 보기 : 7번 진로 상담 concernRequests 가 service  로직에 들어 오는지 확인 하기
        log.info(">>> concernRequests size: {}",
                (concernRequests == null ? null : concernRequests.size()));

        if (concernRequests != null && !concernRequests.isEmpty()) {
            for (ProfileConcernRequestDTO concernReq : concernRequests) {
                if (concernReq == null || concernReq.getConcernCode() == null) {
                    continue;
                }

                String concernCode = concernReq.getConcernCode();

                MasConcern concern = masConcernRepository.findById(concernCode)
                        .orElseThrow(() -> {
                            String message = "존재하지 않는 고민 코드입니다. concernCode=" + concernCode;
                            return new IllegalArgumentException(message);
                        });

                PrfProfileConcernId id = new PrfProfileConcernId(profileId, concernCode);

                PrfProfileConcern profileConcern = PrfProfileConcern.builder()
                        .id(id)
                        .profile(savedProfile)
                        .concern(concern)
                        .detailText(concernReq.getDetailText())
                        .build();

                PrfProfileConcern savedConcern = prfProfileConcernRepository.save(profileConcern);
            }
        }

        // 최종적으로 생성된 프로필 엔티티 반환
        PrfProfile resultProfile = savedProfile;
        return resultProfile;
    }


    /**
     * profile (프로필 정보) 리스트로 가져 오기
     * */
    @Override
    @Transactional(readOnly = true)
    public List<PrfProfile> getProfilesByUser(Long userId) {

        List<PrfProfile> profileList = prfProfileRepository.findByUserUserId(userId);
        return profileList;
    }

    /**
     * 프로필 상세 조회
     */
    /**
     * 프로필 상세 조회
     * - profileId 와 현재 로그인 사용자(currentUserId)를 기준으로
     *   해당 사용자의 프로필인지 검증한 뒤, 상세 정보를 View DTO 로 조립합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public ProfileDetailViewDTO getProfileDetail(Long profileId, Long currentUserId) {

        // 1) 프로필 + 소유자(AppUser) 기본 정보 조회
        PrfProfile profile = prfProfileRepository.findById(profileId)
                .orElseThrow(() ->
                        new EntityNotFoundException("프로필을 찾을 수 없습니다. profileId=" + profileId));

        AppUser owner = profile.getUser();
        if (owner == null) {
            throw new IllegalStateException("프로필에 연결된 사용자 정보가 없습니다. profileId=" + profileId);
        }

        // 현재 사용자 검증: 본인 프로필만 조회 허용
        if (!owner.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("현재 사용자에게 권한이 없는 프로필입니다. profileId=" + profileId);
        }

        // 2) 연관 정보 조회 (스킬 / 가치관 / 근무환경 / 경험 / 고민)

        // 2-1. 스킬 매핑
        List<PrfProfileSkill> skillEntities =
                prfProfileSkillRepository.findByProfile_ProfileId(profileId);

        // 2-2. 직업 가치관 랭킹 (1~3위)
        List<PrfProfileValueRank> valueRankEntities =
                prfProfileValueRankRepository.findByProfile_ProfileIdOrderByRankOrderAsc(profileId);

        // 2-3. 선호 근무환경
        List<PrfProfileWorkEnv> workEnvEntities =
                prfProfileWorkEnvRepository.findByProfile_ProfileId(profileId);

        // 2-4. 보유 경험 (최근 등록일 순)
        List<PrfProfileExperience> experienceEntities =
                prfProfileExperienceRepository.findByProfileProfileId(profileId);

        // 2-5. 진로 고민
        List<PrfProfileConcern> concernEntities =
                prfProfileConcernRepository.findByProfile_ProfileId(profileId);

        // 3) 하위 엔티티들을 View DTO 로 변환

        //보유 스킬 맵핑 : (프로젝트 개발 / 커뮤니케이션 / 마케팅 등등...)
        List<ProfileDetailViewDTO.SkillItemDTO> skillDTOs = skillEntities.stream()
                .map(e -> ProfileDetailViewDTO.SkillItemDTO.builder()
                        .skillCode(e.getSkill().getSkillCd())
                        .skillName(e.getSkill().getSkillName())
                        .skillLevel(e.getSkillLevel())
                        .build())
                .collect(Collectors.toList());

        //직업 가치관 맵핑 : (성장가능성 / 높은연봉 / 자율성 등등...)
        List<ProfileDetailViewDTO.ValueItemDTO> valueDTOs = valueRankEntities.stream()
                .map(e -> ProfileDetailViewDTO.ValueItemDTO.builder()
                        .valueCode(e.getValue().getValueCd())
                        .valueName(e.getValue().getValueName())
                        // DTO 입장에서는 "우선순위" 의미니까 priorityOrder 필드에 rankOrder를 그대로 넣는다

                        .priorityOrder(e.getRankOrder())
                        .build())
                .collect(Collectors.toList());

        //원하는 근무 고민되는 부분 환경 맵핑 : (근무위치 / 나이 / 부족한스킬 등등...)
        List<ProfileDetailViewDTO.WorkEnvItemDTO> workEnvDTOs = workEnvEntities.stream()
                .map(e -> ProfileDetailViewDTO.WorkEnvItemDTO.builder()
                        .workEnvCode(e.getEnv().getEnvCd())
                        .workEnvName(e.getEnv().getEnvName())
                        .build())
                .collect(Collectors.toList());

        //날짜 부분 퐆 형식 커스텀 ??? 엔티티에 베이스 엔티니 넣었는대 굳이 ???? BaseEntity 부분만 건들면 되지 않나 ???
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        //보유 경력 및 포트폴리오 : 과거 실무 경험 및 스킬 능력
        List<ProfileDetailViewDTO.ExperienceItemDTO> experienceDTOs = experienceEntities.stream()
                .map(e -> {
                    String periodText = null;
                    if (e.getStartDate() != null && e.getEndDate() != null) {
                        Period period = Period.between(e.getStartDate(), e.getEndDate());
                        int months = period.getYears() * 12 + period.getMonths();
                        periodText = String.format("%s ~ %s (%d개월)",
                                e.getStartDate().format(dateFormatter),
                                e.getEndDate().format(dateFormatter),
                                months);
                    }

                    ExperienceType type = e.getExperienceType();
                    String typeLabel = switch (type) {
                        case WORK     -> "경력";
                        case PROJECT  -> "프로젝트";
                        case EDU      -> "교육";
                        case CERT     -> "자격";
                        case ACTIVITY -> "활동";
                        default       -> "기타";
                    };

                    return ProfileDetailViewDTO.ExperienceItemDTO.builder()
                            .experienceType(type)
                            .experienceTypeLabel(typeLabel)
                            .title(e.getTitle())
                            .organization(e.getOrganization())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .periodText(periodText)
                            .description(e.getDescription())
                            .outcome(e.getOutcome())
                            .linkUrl(e.getLinkUrl())
                            .build();
                })
                .collect(Collectors.toList());

        List<ProfileDetailViewDTO.ConcernItemDTO> concernDTOs = concernEntities.stream()
                .map(e -> ProfileDetailViewDTO.ConcernItemDTO.builder()
                        .concernCode(e.getConcern().getConcernCd())
                        .concernName(e.getConcern().getConcernName())
                        .detailText(e.getDetailText())
                        .build())
                .collect(Collectors.toList());

        // 4) 상단 한 줄 요약(mainSummary) 생성
        String mainSummary = buildMainSummary(
                owner.getDisplayName(),
                skillDTOs,
                valueDTOs
        );

        // 5) 최종 View DTO 조립 후 반환
        return ProfileDetailViewDTO.builder()
                .profileId(profile.getProfileId())
                .profileTitle(profile.getProfileTitle())
                .profileType(profile.getProfileType())
                .profileTypeLabel(
                        profile.getProfileType() != null
                                ? profile.getProfileType().name()
                                : null
                )
                .regDate(profile.getRegDate())
                .modDate(profile.getModDate())
                .userId(owner.getUserId())
                .displayName(owner.getDisplayName())
                .email(owner.getEmail())
                .userGroup(owner.getUserGroup())
                .mainSummary(mainSummary)
                .skills(skillDTOs)
                .values(valueDTOs)
                .workEnvs(workEnvDTOs)
                .experiences(experienceDTOs)
                .concerns(concernDTOs)
                .build();
    }

    /**
     * 프로필 상단 카드에 표시할 한 줄 요약 텍스트를 생성합니다.
     * - V1 단계에서는 간단한 규칙 기반으로만 구성하고,
     *   추후 AI 기반 요약으로 교체할 수 있는 확장 포인트입니다.
     */
    private String buildMainSummary(
            String displayName,
            List<ProfileDetailViewDTO.SkillItemDTO> skills,
            List<ProfileDetailViewDTO.ValueItemDTO> values
    ) {
        String topSkill = skills.stream()
                .findFirst()
                .map(ProfileDetailViewDTO.SkillItemDTO::getSkillName)
                .orElse("기초 역량");

        String topValue = values.stream()
                .sorted((a, b) -> Integer.compare(a.getPriorityOrder(), b.getPriorityOrder()))
                .findFirst()
                .map(ProfileDetailViewDTO.ValueItemDTO::getValueName)
                .orElse("성장 가능성");

        return String.format(
                "%s 님은 %s 역량을 보유하고, '%s'을(를) 중요하게 생각하는 프로필입니다.",
                displayName,
                topSkill,
                topValue
        );
    }



}

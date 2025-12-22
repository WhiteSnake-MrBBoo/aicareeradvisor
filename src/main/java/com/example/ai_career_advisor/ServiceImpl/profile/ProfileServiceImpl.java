package com.example.ai_career_advisor.ServiceImpl.profile;

import com.example.ai_career_advisor.DTO.profile.request.ProfileConcernRequest;
import com.example.ai_career_advisor.DTO.profile.request.ProfileCreateRequest;
import com.example.ai_career_advisor.DTO.profile.request.ProfileExperienceRequest;
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
import java.util.List;

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
    public PrfProfile createProfileWithDetails(ProfileCreateRequest request) {

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
        List<ProfileExperienceRequest> experienceRequests = request.getExperiences();
        if (experienceRequests != null && !experienceRequests.isEmpty()) {
            for (ProfileExperienceRequest expReq : experienceRequests) {
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
        List<ProfileConcernRequest> concernRequests = request.getConcerns();
        if (concernRequests != null && !concernRequests.isEmpty()) {
            for (ProfileConcernRequest concernReq : concernRequests) {
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



}

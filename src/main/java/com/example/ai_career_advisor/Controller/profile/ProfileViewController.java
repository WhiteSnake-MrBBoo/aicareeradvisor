package com.example.ai_career_advisor.Controller.profile;

import com.example.ai_career_advisor.Constant.ExperienceType;
import com.example.ai_career_advisor.Constant.ProfileType;
import com.example.ai_career_advisor.Constant.UserGroup;

import com.example.ai_career_advisor.DTO.profile.request.ProfileConcernRequestDTO;
import com.example.ai_career_advisor.DTO.profile.request.ProfileCreateRequestDTO;
import com.example.ai_career_advisor.DTO.profile.request.ProfileExperienceRequestDTO;
import com.example.ai_career_advisor.DTO.profile.view.ProfileDetailViewDTO;
import com.example.ai_career_advisor.DTO.profile.view.ProfileFormDTO;

import com.example.ai_career_advisor.Entity.master.MasConcern;
import com.example.ai_career_advisor.Entity.master.MasSkill;
import com.example.ai_career_advisor.Entity.master.MasValue;
import com.example.ai_career_advisor.Entity.master.MasWorkEnv;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import com.example.ai_career_advisor.Entity.user.AppUser;

import com.example.ai_career_advisor.Repository.master.MasConcernRepository;
import com.example.ai_career_advisor.Repository.master.MasSkillRepository;
import com.example.ai_career_advisor.Repository.master.MasValueRepository;
import com.example.ai_career_advisor.Repository.master.MasWorkEnvRepository;

import com.example.ai_career_advisor.Service.profile.ProfileService;
import com.example.ai_career_advisor.Service.user.UserService;
import com.example.ai_career_advisor.Util.UserSessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 프로필 View 컨트롤러
 *
 * 기능
 * - 프로필 입력 폼 진입
 * - 프로필 저장
 * - 프로필 리스트 조회
 *
 * 주의
 * - 현재 단계에서는 Spring Security/Principal을 사용하지 않고,
 *   UserSessionManager를 통해 HttpSession에 저장된 사용자 정보를 활용합니다.
 * - TODO: 추후 보안 적용 시, UserSessionManager 내부 구현을
 *         Principal 기반으로 교체할 예정입니다.
 */
@Slf4j
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileViewController {

    private final UserService userService; //User 로직
    private final ProfileService profileService; //프로필 로직
    private final UserSessionManager userSessionManager; //User Session 용 : package - Util

    private final MasSkillRepository masSkillRepository;
    private final MasValueRepository masValueRepository;
    private final MasWorkEnvRepository masWorkEnvRepository;
    private final MasConcernRepository masConcernRepository; // (추가)

    // JSON 파싱용 ObjectMapper (스프링에서 자동 주입)
    private final ObjectMapper objectMapper;


    /**
     * 프로필 입력 폼 화면
     * - 현재 세션에 선택된 User가 없으면 /user/select로 리다이렉트합니다.
     * - UserGroup 정보를 기반으로 ProfileType 기본값을 설정합니다.
     */
    @GetMapping("/new")
    public String showProfileForm(HttpSession session, Model model) {

        Long currentUserId = userSessionManager.getCurrentUserId(session);
        if (currentUserId == null) {
            // 아직 사용자를 선택하지 않았다면, 먼저 사용자 선택 화면으로 이동
            String redirectPath = "redirect:/user/select";
            return redirectPath;
        }

        Optional<AppUser> userOptional = userService.findById(currentUserId);
        if (userOptional.isEmpty()) {
            // 세션에 잘못된 사용자 ID가 남아 있는 경우, 세션 초기화 후 사용자 선택으로 보냄
            userSessionManager.clearCurrentUser(session);
            String redirectPath = "redirect:/user/select";
            return redirectPath;
        }

        AppUser currentUser = userOptional.get();

        // --- 1) 폼 기본값 설정 ---
        ProfileFormDTO form = new ProfileFormDTO();

        // 프로필 제목 기본값: "이름 - 기본 커리어 프로필" 형식
        String defaultTitle = currentUser.getDisplayName() + " - 기본 커리어 프로필";
        form.setProfileTitle(defaultTitle);

        // UserGroup → ProfileType 기본 매핑 (초기 하드코딩 룰)
        // TODO: 추후 ProfileType이 세분화되면 별도 매핑 로직/설정으로 분리
        UserGroup userGroup = currentUser.getUserGroup();
        ProfileType defaultProfileType = mapUserGroupToProfileType(userGroup);
        form.setProfileType(defaultProfileType);

        // --- 2) 마스터 데이터 로딩 (스킬/가치관/근무환경) ---
        List<MasSkill> skillList = masSkillRepository.findAll();
        List<MasValue> valueList = masValueRepository.findAll();
        List<MasWorkEnv> workEnvList = masWorkEnvRepository.findAll();
        List<MasConcern> concernList = masConcernRepository.findAll(); //추가


        // --- 3) 모델에 데이터 바인딩 ---
        model.addAttribute("profileForm", form);
        model.addAttribute("skillList", skillList);
        model.addAttribute("valueList", valueList);
        model.addAttribute("workEnvList", workEnvList);
        model.addAttribute("concernList", concernList);//추가

        // 상단에 표시할 현재 사용자 정보
        model.addAttribute("currentUserName", currentUser.getDisplayName());
        model.addAttribute("currentUserGroup", currentUser.getUserGroup());

        String viewName = "profile/profile-form";
        return viewName;
    }

    /**
     * 프로필 저장 처리
     * - ProfileForm(View용 DTO)를 ProfileCreateRequest(Service용 DTO)로 변환한 뒤,
     *   ProfileService를 통해 저장합니다.
     * - 저장 후에는 해당 사용자의 프로필 리스트 화면으로 리다이렉트합니다.
     */
    /**
     * 프로필 저장 처리
     * - ProfileForm(View용 DTO)를 ProfileCreateRequest(Service용 DTO)로 변환한 뒤,
     *   ProfileService를 통해 저장합니다.
     * - 저장 후에는 해당 사용자의 프로필 리스트 화면으로 리다이렉트합니다.
     */
    @PostMapping("/save")
    public String saveProfile(
            HttpSession session,
            @ModelAttribute("profileForm") ProfileFormDTO form
    ) {

        //session에 있는 정보 가져오기 PK 기준
        Long currentUserId = userSessionManager.getCurrentUserId(session);
        if (currentUserId == null) {
            String redirectPath = "redirect:/user/select";
            return redirectPath;
        }

        //session 정보를 기준으로 user pk 가져 오기
        Optional<AppUser> userOptional = userService.findById(currentUserId);
        if (userOptional.isEmpty()) {
            userSessionManager.clearCurrentUser(session);
            String redirectPath = "redirect:/user/select";
            return redirectPath;
        }

        AppUser currentUser = userOptional.get();

        // --- 1) View용 폼(ProfileForm)을 Service용 DTO(ProfileCreateRequest)로 변환 ---

        ProfileCreateRequestDTO request = new ProfileCreateRequestDTO();

        // 1-1. User 정보 (현재 단계에서는 세션의 User 정보를 그대로 사용)
        request.setDisplayName(currentUser.getDisplayName());
        request.setEmail(currentUser.getEmail());
        request.setUserGroup(currentUser.getUserGroup());

        // 1-2. 프로필 기본 정보
        request.setProfileTitle(form.getProfileTitle());
        request.setProfileType(form.getProfileType());

        // 1-3. 스킬 코드 리스트
        request.setSkillCodes(form.getSelectedSkillCodes());

        // 1-4. 가치관 Top3 (null/빈 값은 제외)
        List<String> valueCodesOrdered = new ArrayList<>();
        if (form.getSelectedValueCode1() != null && !form.getSelectedValueCode1().isBlank()) {
            valueCodesOrdered.add(form.getSelectedValueCode1());
        }
        if (form.getSelectedValueCode2() != null && !form.getSelectedValueCode2().isBlank()) {
            valueCodesOrdered.add(form.getSelectedValueCode2());
        }
        if (form.getSelectedValueCode3() != null && !form.getSelectedValueCode3().isBlank()) {
            valueCodesOrdered.add(form.getSelectedValueCode3());
        }
        request.setValueCodesOrdered(valueCodesOrdered);

        // 1-5. 진로 후보 코드(우선순위 1~3, 향후 직업 마스터와 연동 예정)
        List<String> careerOptionCodesOrdered = new ArrayList<>();
        if (form.getCareerOptionCode1() != null && !form.getCareerOptionCode1().isBlank()) {
            careerOptionCodesOrdered.add(form.getCareerOptionCode1());
        }
        if (form.getCareerOptionCode2() != null && !form.getCareerOptionCode2().isBlank()) {
            careerOptionCodesOrdered.add(form.getCareerOptionCode2());
        }
        if (form.getCareerOptionCode3() != null && !form.getCareerOptionCode3().isBlank()) {
            careerOptionCodesOrdered.add(form.getCareerOptionCode3());
        }
        request.setCareerOptionCodesOrdered(careerOptionCodesOrdered);

        // 1-6. 선호 근무환경 코드 리스트
        request.setWorkEnvCodes(form.getSelectedWorkEnvCodes());

        // 1-7. 보유 경험 리스트(JSON -> DTO 리스트)
        List<ProfileExperienceRequestDTO> experienceRequests = new ArrayList<>();

        String experienceJson = form.getExperienceJson();
        boolean hasExperienceJson = StringUtils.hasText(experienceJson);

        if (hasExperienceJson) {
            try {
                // JSON 문자열을 List<ExperienceJsonPayload>로 파싱
                List<ExperienceJsonPayload> payloadList =
                        objectMapper.readValue(experienceJson, new TypeReference<List<ExperienceJsonPayload>>() {});

                for (ExperienceJsonPayload payload : payloadList) {

                    // 1) ExperienceType 변환 (문자열 → Enum)
                    ExperienceType experienceType = null;
                    if (StringUtils.hasText(payload.getType())) {
                        try {
                            experienceType = ExperienceType.valueOf(payload.getType());
                        } catch (IllegalArgumentException ex) {
                            // TODO: 필요 시 로깅 및 기본값 처리
                            // 잘못된 값이 들어온 경우에는 null로 두거나, ExperienceType.WORK 등으로 대체 가능
                        }
                    }

                    // 2) 날짜 변환 (String → LocalDate)
                    LocalDate startDate = null;
                    LocalDate endDate = null;

                    if (StringUtils.hasText(payload.getStartDate())) {
                        startDate = LocalDate.parse(payload.getStartDate());
                    }
                    if (StringUtils.hasText(payload.getEndDate())) {
                        endDate = LocalDate.parse(payload.getEndDate());
                    }

                    // 3) Service 계층에서 사용하는 ProfileExperienceRequest 생성
                    ProfileExperienceRequestDTO expRequest = ProfileExperienceRequestDTO.builder()
                            .experienceType(experienceType)
                            .title(payload.getTitle())
                            .organization(payload.getOrganization())
                            .startDate(startDate)
                            .endDate(endDate)
                            .description(payload.getDescription())
                            .outcome(payload.getOutcome())
                            .linkUrl(payload.getLinkUrl())
                            .build();

                    experienceRequests.add(expRequest);
                }

            } catch (JsonProcessingException e) {
                // JSON 파싱 실패 시: 전체 요청을 막지는 않고, 로그만 남깁니다.
                // 필요하다면 BindingResult에 에러를 추가해서 화면에 표시하는 것도 가능
                // log.warn("경험 JSON 파싱 중 오류 발생", e);

            }

        }

        request.setExperiences(experienceRequests);


        // 1-8. 진로 고민 → 선택된 코드마다 동일한 상세 설명을 부여
        // 서비스 로직 저장 로직 까지는 구현 되어 있으나 체크박스 mas_concern이 정해 지지 않았음
        List<ProfileConcernRequestDTO> concerns = new ArrayList<>();

        //view 폼에 있는 선택 진로 상담 부분이 들어 오는지 체크 사항 로그
        log.info(">>> form.getSelectedConcernCodes() size: {}",
                (form.getSelectedConcernCodes() == null ? null : form.getSelectedConcernCodes().size()));


        if (form.getSelectedConcernCodes() != null && !form.getSelectedConcernCodes().isEmpty()) {
            for (String concernCode : form.getSelectedConcernCodes()) {

                ProfileConcernRequestDTO concernRequest = ProfileConcernRequestDTO.builder()
                        .concernCode(concernCode)
                        .detailText(form.getConcernDetailText())
                        .build();

                concerns.add(concernRequest);
            }
        }
        request.setConcerns(concerns);

        // --- 2) 프로필 생성 서비스 호출 ---
        PrfProfile createdProfile = profileService.createProfileWithDetails(request);

        // TODO: 필요 시 생성된 profileId를 FlashAttribute 등으로 전달 가능
        Long createdProfileId = createdProfile.getProfileId();

        // --- 3) 프로필 리스트 화면으로 이동 ---
        String redirectPath = "redirect:/profile/list";
        return redirectPath;
    }


    /**
     * 현재 선택된 사용자의 프로필 리스트 화면
     */
    @GetMapping("/list")
    public String showProfileList(HttpSession session, Model model) {

        Long currentUserId = userSessionManager.getCurrentUserId(session);
        if (currentUserId == null) {
            String redirectPath = "redirect:/user/select";
            return redirectPath;
        }

        Optional<AppUser> userOptional = userService.findById(currentUserId);
        if (userOptional.isEmpty()) {
            userSessionManager.clearCurrentUser(session);
            String redirectPath = "redirect:/user/select";
            return redirectPath;
        }

        AppUser currentUser = userOptional.get();

        // 해당 사용자의 프로필 목록 조회
        List<PrfProfile> profileList = profileService.getProfilesByUser(currentUserId);

        model.addAttribute("profileList", profileList);
        model.addAttribute("currentUserName", currentUser.getDisplayName());
        model.addAttribute("currentUserGroup", currentUser.getUserGroup());

        String viewName = "profile/profile-list";
        return viewName;
    }

    /**
     * UserGroup → ProfileType 매핑 (초기 하드코딩 버전)
     *
     * TODO:
     *  - ProfileType이 세분화되거나, 사용자 입력에 따라 달라져야 한다면
     *    별도 설정/전략 클래스로 분리하는 것을 고려합니다.
     */
    private ProfileType mapUserGroupToProfileType(UserGroup userGroup) {

        if (userGroup == null) {
            // 기본값: 성인 구직자 프로필로 처리
            return ProfileType.ADULT_JOBSEEKER;
        }

        switch (userGroup) {
            case ADULT:
                return ProfileType.ADULT_JOBSEEKER;
            case YOUTH:
                return ProfileType.HIGHSCHOOL;
            default:
                return ProfileType.ADULT_JOBSEEKER;
        }
    }

    /**
     * 프론트에서 넘어오는 6.경험 및 역량 JSON 1건에 대한 내부 파싱 DTO
     * - ProfileExperienceRequest로 매핑하기 위한 중간 객체입니다.
     * 6번 경헙 및 역량을 다중 입려시 받아올 임시 DTO
     */
    @Getter
    @Setter
    private static class ExperienceJsonPayload {
        private String type;
        private String title;
        private String organization;
        private String startDate;
        private String endDate;
        private String description;
        private String outcome;
        private String linkUrl;
    }

    /**
     * 프로필 상세 화면
     * - /profile/{profileId}
     * - 현재 세션의 사용자와 프로필 소유자를 검증한 뒤 상세 정보를 보여줍니다.
     */
    @GetMapping("/{profileId}")
    public String viewProfileDetail(@PathVariable Long profileId,
                                    HttpSession session,
                                    Model model) {

        Long currentUserId = userSessionManager.getCurrentUserId(session);
        if (currentUserId == null) {
            return "redirect:/user/select";
        }

        ProfileDetailViewDTO detail =
                profileService.getProfileDetail(profileId, currentUserId);

        model.addAttribute("profileDetail", detail);

        // 추후 탭(자기소개서/AI 분석 등)에 활용할 profileId도 함께 전달
        model.addAttribute("profileId", profileId);

        return "profile/profile-detail"; // templates/profile/profile-detail.html
    }


}

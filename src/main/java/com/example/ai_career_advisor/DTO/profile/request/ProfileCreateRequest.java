package com.example.ai_career_advisor.DTO.profile.request;

import com.example.ai_career_advisor.Constant.ProfileType;
import com.example.ai_career_advisor.Constant.UserGroup;
import java.util.List;
import lombok.*;

/**
 * 프로필 + 사용자 + 기본 매핑까지 한 번에 생성하는 요청 DTO
 * - 1차 테스트용 파이프라인에서 사용합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCreateRequest {

    // --- User 정보 (테스트용 식별) ---
    private String displayName;
    private String email;
    private UserGroup userGroup;

    // --- 프로필 기본 정보 ---
    private ProfileType profileType;
    private String profileTitle;

    // --- 스킬(코드 리스트) ---
    private List<String> skillCodes;

    // --- 가치관 Top N(코드 리스트, 순서 = 랭킹) ---
    private List<String> valueCodesOrdered;

    // --- 진로 후보(코드 리스트, 순서 = 우선순위) ---
    private List<String> careerOptionCodesOrdered;

    // --- 선호 근무환경(코드 리스트) ---
    private List<String> workEnvCodes;

    // --- 경험(1:N) ---
    private List<ProfileExperienceRequest> experiences;

    // --- 고민(다중 선택 + 상세) ---
    private List<ProfileConcernRequest> concerns;
}

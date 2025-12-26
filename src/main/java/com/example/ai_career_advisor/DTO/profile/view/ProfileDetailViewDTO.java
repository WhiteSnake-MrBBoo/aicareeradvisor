package com.example.ai_career_advisor.DTO.profile.view;

import com.example.ai_career_advisor.Constant.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 프로필 상세 화면(View)을 위한 통합 DTO
 * - 하나의 프로필에 대한 모든 정보를 한 번에 조회하여 전달합니다.
 * - Entity 를 직접 노출하지 않고, View 에 필요한 형태로 평탄화한 구조입니다.
 */
@Data
@Builder
public class ProfileDetailViewDTO {

    // --- 기본 프로필 정보 ---
    private Long profileId;
    private String profileTitle;
    private ProfileType profileType;
    private String profileTypeLabel;   // 화면용 한글 라벨 (예: "성인 구직자 기본 프로필")
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    // --- 사용자 기본 정보 ---
    private Long userId;
    private String displayName;
    private String email;
    private UserGroup userGroup;

    // --- 요약 정보(상단 카드/헤더용) ---
    private String mainSummary;        // 예: "백엔드 지향, Java/AI 경험, 성장 가능성 중시" 등

    // --- 상세 섹션들 ---

    /** 보유 스킬 리스트 */
    private List<SkillItemDTO> skills;

    /** 직업 가치관 Top3 */
    private List<ValueItemDTO> values;

    /** 선호 근무 환경 */
    private List<WorkEnvItemDTO> workEnvs;

    /** 진로 고민 리스트 */
    private List<ConcernItemDTO> concerns;

    /** 추가 영역 : 보유 경험 리스트(경력/프로젝트/교육/자격/활동 등) */
    private List<ExperienceItemDTO> experiences;



    // ================= 하위 DTO들 : 1 회성 DTO =================

    @Data
    @Builder
    public static class SkillItemDTO {
        private String skillCode;
        private String skillName;
        private SkillLevel skillLevel;  // BASIC / INTERMEDIATE / ADVANCED
    }

    @Data
    @Builder
    public static class ValueItemDTO {
        private String valueCode;
        private String valueName;
        private int priorityOrder;      // 1,2,3 순서
    }

    @Data
    @Builder
    public static class WorkEnvItemDTO {
        private String workEnvCode;
        private String workEnvName;
    }

    @Data
    @Builder
    public static class ExperienceItemDTO {
        private ExperienceType experienceType;  // WORK / PROJECT / EDU / CERT / ACTIVITY
        private String experienceTypeLabel;     // 화면 표시용 한글 라벨
        private String title;
        private String organization;
        private LocalDate startDate;
        private LocalDate endDate;
        private String periodText;             // "2024.01 ~ 2024.06 (6개월)" 등, 가공 텍스트
        private String description;
        private String outcome;
        private String linkUrl;
    }

    @Data
    @Builder
    public static class ConcernItemDTO {
        private String concernCode;
        private String concernName;
        private String detailText;             // 사용자가 적은 고민 구체 내용
    }
}

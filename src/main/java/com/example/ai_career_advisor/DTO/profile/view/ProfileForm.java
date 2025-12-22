package com.example.ai_career_advisor.DTO.profile.view;

import com.example.ai_career_advisor.Constant.ExperienceType;
import com.example.ai_career_advisor.Constant.ProfileType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 프로필 입력 화면(Thymeleaf) 전용 폼 DTO
 *
 * - View에서 입력받는 필드만 담습니다.
 * - Service에서 사용하는 ProfileCreateRequest로 변환하는 용도로 사용됩니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProfileForm {

    // ----------------------------------------------------------------------
    // 1. 기본 정보
    // ----------------------------------------------------------------------

    /** 프로필 제목 */
    private String profileTitle;

    /** 프로필 타입 (예: ADULT_JOBSEEKER, HIGH_SCHOOL 등) */
    private ProfileType profileType;

    // ----------------------------------------------------------------------
    // 2. 보유 스킬
    // ----------------------------------------------------------------------

    /**
     * 선택된 스킬 코드들
     * - Checkbox 그룹과 바인딩됩니다.
     */
    private List<String> selectedSkillCodes = new ArrayList<>();

    // ----------------------------------------------------------------------
    // 3. 직업 가치관 Top3
    // ----------------------------------------------------------------------

    /** 1순위 직업 가치관 코드 */
    private String selectedValueCode1;

    /** 2순위 직업 가치관 코드 */
    private String selectedValueCode2;

    /** 3순위 직업 가치관 코드 */
    private String selectedValueCode3;

    // ----------------------------------------------------------------------
    // 4. 진로 후보 (우선순위 1~3)
    // ----------------------------------------------------------------------

    /**
     * 진로 후보 코드(우선순위 1~3)
     *
     * 현재는 Work24/직업 마스터가 없으므로 free text로 사용합니다.
     * 추후 직업 마스터/Work24와 연결되면 코드 기반으로 교체할 예정입니다.
     */
    private String careerOptionCode1;
    private String careerOptionCode2;
    private String careerOptionCode3;

    // ----------------------------------------------------------------------
    // 5. 선호 근무환경
    // ----------------------------------------------------------------------

    /**
     * 선호 근무환경 코드 리스트
     * - Checkbox 그룹과 바인딩됩니다.
     */
    private List<String> selectedWorkEnvCodes = new ArrayList<>();

    // ----------------------------------------------------------------------
    // 6. 보유 경험 (1건 입력 버전)
    // ----------------------------------------------------------------------

    /**
     * 경험 유형
     * 예: FULL_TIME_JOB, INTERNSHIP, PROJECT, EDUCATION 등
     */
    private ExperienceType experienceType;

    /** 경험 제목 (예: 회사명 + 직무, 프로젝트명 등) */
    private String experienceTitle;

    /** 조직/기관명 (회사, 학교, 교육기관 등) */
    private String experienceOrganization;

    /** 시작일 */
    private LocalDate experienceStartDate;

    /** 종료일 */
    private LocalDate experienceEndDate;

    /** 상세 설명 */
    private String experienceDescription;

    /** 성과/결과 */
    private String experienceOutcome;

    /**
     * 관련 링크 (포트폴리오, GitHub, Notion 등)
     */
    private String experienceLinkUrl;


    // ----------------------------------------------------------------------
    // 6. 보유 경험 (다건 입력용 JSON)
    // ----------------------------------------------------------------------

    /**
     * 보유 경험 리스트(JSON 문자열)
     *
     * - 프론트에서 experiences 배열을 JSON.stringify() 한 값을 담습니다.
     * - Controller에서 ObjectMapper로 파싱해서
     *   List<ProfileExperienceRequest>로 변환하여 Service로 전달합니다.
     *
     * JSON 구조 예시:
     * [
     *   {
     *     "type": "WORK",
     *     "title": "백엔드 개발자 인턴",
     *     "organization": "OOO 주식회사",
     *     "startDate": "2024-01-01",
     *     "endDate": "2024-06-30",
     *     "description": "...",
     *     "outcome": "...",
     *     "linkUrl": "https://..."
     *   },
     *   { ... }
     * ]
     */
    private String experienceJson;

    // ----------------------------------------------------------------------
    // 7. 진로 고민
    // ----------------------------------------------------------------------

    /**
     * 선택된 고민 코드들
     * - Checkbox 그룹과 바인딩됩니다.
     */
    private List<String> selectedConcernCodes = new ArrayList<>();

    /**
     * 고민에 대한 추가 설명(공통)
     * - 여러 고민 코드에 공통으로 적용되는 상세 설명입니다.
     * - 추후 필요 시 코드별 상세 설명 구조로 확장 가능합니다.
     */
    private String concernDetailText;
}

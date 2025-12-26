package com.example.ai_career_advisor.DTO.ai;

import com.example.ai_career_advisor.Constant.AiScenario;
import com.example.ai_career_advisor.Constant.ProfileType;
import com.example.ai_career_advisor.Constant.UserGroup;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 자기소개서(AI) 생성을 위한 입력 DTO
 * - ProfileDetailViewDTO 에서 필요한 정보만 뽑아 요약한 형태입니다.
 */
@Data
@Builder
public class AiEssayRequestDTO {

    // 기본 식별자
    private Long profileId;
    private Long userId;

    // 사용자/프로필 기본 정보
    private String displayName;
    private ProfileType profileType;
    private UserGroup userGroup;

    // 희망 직무/커리어 요약
    private String targetTitle;      // 예: "백엔드 개발자"
    private String profileSummary;   // 프로필 상단 요약문 (mainSummary 등)

    // 핵심 요소 요약
    private List<String> topSkills;          // 상위 N개 스킬 이름
    private List<String> topValues;          // 가치관 Top3 이름
    private List<String> keyExperiences;     // 경험 한 줄 요약 리스트
    private String mainConcernSummary;       // 진로 고민 요약(선택)

    // 시나리오 (지금은 ESSAY 고정, 향후 확장 대비) : AiScenario 에 Enum 에서 맞춤형 상수값 가져 오기
    private AiScenario scenario;
}

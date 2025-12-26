package com.example.ai_career_advisor.Util.ai;

import com.example.ai_career_advisor.Constant.AiScenario;
import com.example.ai_career_advisor.DTO.ai.AiEssayRequestDTO;
import com.example.ai_career_advisor.DTO.profile.view.ProfileDetailViewDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 프로필 상세 DTO → AI 자기소개서 요청 DTO 변환 유틸리티
 *
 * 목적
 * - View 전용 ProfileDetailViewDTO 를 직접 AI 서비스에 넘기지 않고,
 *   AI 프롬프트에 적합한 구조(AiEssayRequestDTO)로 변환하는 책임을 분리합니다.
 */
@Component
public class AiEssayMapper {

    /**
     * ProfileDetailViewDTO 기반으로 자기소개서 생성 요청 DTO를 구성합니다.
     *
     * @param detailView 프로필 상세 View DTO
     * @return AiEssayRequestDTO (AiScenario.ESSAY 고정)
     */
    public AiEssayRequestDTO fromProfileDetail(ProfileDetailViewDTO detailView) {

        // 1) 상위 스킬 N개 추출 (우선 3개만 사용)
        List<String> topSkills = new ArrayList<>();
        if (detailView.getSkills() != null) {
            detailView.getSkills().stream()
                    .limit(3)
                    .forEach(skillItem ->
                            topSkills.add(skillItem.getSkillName())
                    );
        }

        // 2) 가치관 Top3 이름 추출 (이미 정렬된 상태라고 가정)
        List<String> topValues = new ArrayList<>();
        if (detailView.getValues() != null) {
            detailView.getValues().stream()
                    .sorted((a, b) -> Integer.compare(a.getPriorityOrder(), b.getPriorityOrder()))
                    .limit(3)
                    .forEach(valueItem ->
                            topValues.add(valueItem.getValueName())
                    );
        }

        // 3) 보유 경험 한 줄 요약 리스트 구성
        List<String> keyExperiences = new ArrayList<>();
        if (detailView.getExperiences() != null) {
            detailView.getExperiences().forEach(exp -> {
                StringBuilder sb = new StringBuilder();

                if (exp.getStartDate() != null || exp.getEndDate() != null) {
                    sb.append("[기간] ");
                    if (exp.getStartDate() != null) {
                        sb.append(exp.getStartDate());
                    }
                    sb.append(" ~ ");
                    if (exp.getEndDate() != null) {
                        sb.append(exp.getEndDate());
                    }
                    sb.append(" / ");
                }

                if (exp.getExperienceTypeLabel() != null) {
                    sb.append("[").append(exp.getExperienceTypeLabel()).append("] ");
                }

                if (exp.getTitle() != null) {
                    sb.append(exp.getTitle()).append(" - ");
                }

                if (exp.getOrganization() != null) {
                    sb.append(exp.getOrganization()).append(" / ");
                }

                if (exp.getOutcome() != null) {
                    sb.append("성과: ").append(exp.getOutcome());
                }

                String summary = sb.toString().trim();
                if (!summary.isEmpty()) {
                    keyExperiences.add(summary);
                }
            });
        }

        // 4) 진로 고민 요약 (첫 번째 고민 기준으로 간단 요약)
        String mainConcernSummary = null;
        if (detailView.getConcerns() != null && !detailView.getConcerns().isEmpty()) {
            ProfileDetailViewDTO.ConcernItemDTO first = detailView.getConcerns().get(0);

            StringBuilder sb = new StringBuilder();
            if (first.getConcernName() != null) {
                sb.append(first.getConcernName());
            }
            if (first.getDetailText() != null && !first.getDetailText().isBlank()) {
                if (!sb.isEmpty()) {
                    sb.append(" - ");
                }
                sb.append(first.getDetailText());
            }

            mainConcernSummary = sb.toString();
        }

        // 5) 희망 직무/타이틀은 우선 profileTitle 을 그대로 사용
        String targetTitle = detailView.getProfileTitle();

        AiEssayRequestDTO requestDTO = AiEssayRequestDTO.builder()
                .profileId(detailView.getProfileId())
                .userId(detailView.getUserId())
                .displayName(detailView.getDisplayName())
                .profileType(detailView.getProfileType())
                .userGroup(detailView.getUserGroup())
                .targetTitle(targetTitle)
                .profileSummary(detailView.getMainSummary())
                .topSkills(topSkills)
                .topValues(topValues)
                .keyExperiences(keyExperiences)
                .mainConcernSummary(mainConcernSummary)
                .scenario(AiScenario.ESSAY) //자기 소계서 Enum 프롬프트 활용 Enum 사용
                .build();

        return requestDTO;
    }
}

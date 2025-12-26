package com.example.ai_career_advisor.DTO.ai;

import com.example.ai_career_advisor.Constant.AiScenario;
import lombok.Builder;
import lombok.Data;

/**
 * 자기소개서(AI) 생성 결과 DTO
 * - 우선은 전체 본문만 사용하고, 추후 단락 분리 등 확장 가능합니다.
 */
@Data
@Builder
public class AiEssayResultDTO {

    private Long profileId;
    private AiScenario scenario;

    /**
     * 전체 자기소개서 본문
     */
    private String rawEssayText;

    // 필요 시 단락별 필드를 추가해서 확장 가능
    // private String introParagraph;
    // private String strengthParagraph;
    // private String episodeParagraph;
    // private String closingParagraph;
}

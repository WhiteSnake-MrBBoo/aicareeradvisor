package com.example.ai_career_advisor.Constant;

/**
 * AI 생성/분석 시나리오 구분 Enum
 * - 프롬프트 템플릿 선택, 모델 옵션 선택 등에 사용합니다.
 */
public enum AiScenario {

    ESSAY("자기소개서 생성", "SCN_ESSAY"),
    CAREER_ANALYSIS("커리어 분석 V1", "SCN_CAREER"),
    WORK24_FEEDBACK("Work24 연동 피드백", "SCN_WORK24");

    /**
     * 화면/로그용 설명
     */
    private final String displayName;

    /**
     * 프롬프트/설정 맵핑용 키 (추후 외부 설정 파일로 뺄 때 사용)
     */
    private final String promptKey;

    AiScenario(String displayName, String promptKey) {
        this.displayName = displayName;
        this.promptKey = promptKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPromptKey() {
        return promptKey;
    }
}

package com.example.ai_career_advisor.Constant;

/**
 * 프로필 타입(상세 분류)
 * - 사용자군(UserGroup: ADULT/YOUTH)보다 더 구체적인 타입입니다.
 * - UI/입력 필드 노출 규칙, 분석 룰(가중치/추천 기준) 분기에 활용합니다.
 */
public enum ProfileType {
    ADULT_JOBSEEKER, //성인
    HIGHSCHOOL, //고등학생
    MIDDLESCHOOL, //중학생
    ELEMENTARY, //초등학생
    UNIVERSITY //대학생
}

package com.example.ai_career_advisor.Service.ai;

import com.example.ai_career_advisor.DTO.ai.AiEssayRequestDTO;
import com.example.ai_career_advisor.DTO.ai.AiEssayResultDTO;

/**
 * 자기소개서(AI) 생성 서비스 인터페이스
 *
 * - 현재는 Stub 구현을 사용하고,
 *   추후 OpenAI / 사내 LLM 연동 시 구현체만 교체합니다.
 */
public interface AiEssayService {

    /**
     * 프로필 기반 자기소개서 초안을 생성합니다.
     *
     * @param requestDTO 프로필 요약 정보가 담긴 요청 DTO
     * @return 생성된 자기소개서 결과 DTO
     */
    AiEssayResultDTO generateEssay(AiEssayRequestDTO requestDTO);
}

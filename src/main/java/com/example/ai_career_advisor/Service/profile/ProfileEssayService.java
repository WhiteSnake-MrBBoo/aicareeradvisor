package com.example.ai_career_advisor.Service.profile;

import com.example.ai_career_advisor.DTO.ai.AiEssayResultDTO;
import com.example.ai_career_advisor.DTO.profile.ai.ProfileEssaySummaryDTO;

import java.util.List;

/**
 * 프로필 자기소개서 버전 관리 & 생성 서비스
 */
public interface ProfileEssayService {

    /**
     * 프로필/유저 정보를 검증하고,
     * AI를 통해 자소서 초안을 생성한 뒤 DB에 새로운 버전으로 저장합니다.
     *
     * @param profileId     대상 프로필 ID
     * @param currentUserId 세션의 사용자 ID (권한/소유자 검증용)
     * @return 생성된 자기소개서 결과 DTO (화면 출력용)
     */
    AiEssayResultDTO generateAndSaveEssay(Long profileId, Long currentUserId);

    /**
     * 특정 프로필에 저장된 자소서 버전 목록을 반환합니다.
     *
     * @param profileId 프로필 ID
     * @param currentUserId 세션 사용자 ID
     * @return 자소서 요약 목록
     */
    List<ProfileEssaySummaryDTO> getEssaySummaries(Long profileId, Long currentUserId);


    /**
     * 프로필의 "대표 자소서"를 조회합니다.
     * - is_main = Y 가 있으면 그 버전을 사용
     * - 없으면 null 반환 (화면에서는 "아직 대표 자소서가 없습니다" 메시지 처리)
     */
    AiEssayResultDTO getMainEssayResult(Long profileId, Long currentUserId);

}

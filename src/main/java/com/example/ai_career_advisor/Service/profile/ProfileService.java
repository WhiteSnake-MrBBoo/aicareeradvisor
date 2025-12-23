package com.example.ai_career_advisor.Service.profile;

import com.example.ai_career_advisor.DTO.profile.request.ProfileCreateRequestDTO;
import com.example.ai_career_advisor.DTO.profile.view.ProfileDetailViewDTO;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;

import java.util.List;

/**
 * 프로필 도메인 서비스
 * - 프로필 생성 + 매핑 저장 파이프라인을 담당합니다.
 */
public interface ProfileService {

    PrfProfile createProfileWithDetails(ProfileCreateRequestDTO request);

    /**
     * 특정 사용자가 가진 모든 프로필을 조회합니다.
     *
     * @param userId AppUser PK
     * @return 해당 사용자의 프로필 목록
     */
    List<PrfProfile> getProfilesByUser(Long userId);

    /**
     * 프로필 상세 조회
     * - profileId 와 현재 사용자 userId 를 기준으로,
     *   해당 사용자의 프로필인지 검증 후 상세 정보를 반환합니다.
     */
    ProfileDetailViewDTO getProfileDetail(Long profileId, Long currentUserId);

}

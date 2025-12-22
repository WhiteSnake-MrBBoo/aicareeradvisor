package com.example.ai_career_advisor.Service.profile;

import com.example.ai_career_advisor.DTO.profile.request.ProfileCreateRequest;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;

import java.util.List;

/**
 * 프로필 도메인 서비스
 * - 프로필 생성 + 매핑 저장 파이프라인을 담당합니다.
 */
public interface ProfileService {

    PrfProfile createProfileWithDetails(ProfileCreateRequest request);

    /**
     * 특정 사용자가 가진 모든 프로필을 조회합니다.
     *
     * @param userId AppUser PK
     * @return 해당 사용자의 프로필 목록
     */
    List<PrfProfile> getProfilesByUser(Long userId);

}

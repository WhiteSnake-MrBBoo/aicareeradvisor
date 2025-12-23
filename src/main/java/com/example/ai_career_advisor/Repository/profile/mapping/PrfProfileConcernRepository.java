package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileConcern;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileConcernId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-진로 고민 매핑 리포지토리
 * - mas_concern(고민 마스터)와 프로필을 매핑합니다.
 * - 1개의 프로필에 여러 고민 코드가 연결될 수 있습니다.
 */
public interface PrfProfileConcernRepository
        extends JpaRepository<PrfProfileConcern, PrfProfileConcernId> {

    /**
     * 특정 프로필에 연결된 모든 고민 매핑 조회
     *  - profile.profileId = :profileId
     */
    List<PrfProfileConcern> findByProfile_ProfileId(Long profileId);
}

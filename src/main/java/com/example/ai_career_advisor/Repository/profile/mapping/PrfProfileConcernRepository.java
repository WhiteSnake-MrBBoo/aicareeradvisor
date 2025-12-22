package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileConcern;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileConcernId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-진로 고민 매핑 리포지토리
 */
public interface PrfProfileConcernRepository extends JpaRepository<PrfProfileConcern, PrfProfileConcernId> {

    List<PrfProfileConcern> findByProfileProfileId(Long profileId);
}

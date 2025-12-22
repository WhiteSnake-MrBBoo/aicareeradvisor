package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileWorkEnv;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileWorkEnvId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-근무환경 매핑 리포지토리
 */
public interface PrfProfileWorkEnvRepository extends JpaRepository<PrfProfileWorkEnv, PrfProfileWorkEnvId> {

    List<PrfProfileWorkEnv> findByProfileProfileId(Long profileId);
}

package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileSkill;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileSkillId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-스킬 매핑 리포지토리
 */
public interface PrfProfileSkillRepository extends JpaRepository<PrfProfileSkill, PrfProfileSkillId> {

    List<PrfProfileSkill> findByProfileProfileId(Long profileId);
}

package com.example.ai_career_advisor.Repository.profile.experience;

import com.example.ai_career_advisor.Entity.profile.experience.PrfProfileExperience;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필 경험 리포지토리
 */
public interface PrfProfileExperienceRepository extends JpaRepository<PrfProfileExperience, Long> {

    List<PrfProfileExperience> findByProfileProfileId(Long profileId);
}

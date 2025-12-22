package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileCareerPriority;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileCareerPriorityId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-진로 후보 우선순위 매핑 리포지토리
 */
public interface PrfProfileCareerPriorityRepository
        extends JpaRepository<PrfProfileCareerPriority, PrfProfileCareerPriorityId> {

    List<PrfProfileCareerPriority> findByProfileProfileIdOrderByPriorityOrderAsc(Long profileId);
}

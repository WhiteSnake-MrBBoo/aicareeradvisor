package com.example.ai_career_advisor.Repository.profile.core;

import com.example.ai_career_advisor.Entity.profile.core.PrfProfileGoal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필 목표(희망 커리어 방향) 리포지토리
 */
public interface PrfProfileGoalRepository extends JpaRepository<PrfProfileGoal, Long> {
}

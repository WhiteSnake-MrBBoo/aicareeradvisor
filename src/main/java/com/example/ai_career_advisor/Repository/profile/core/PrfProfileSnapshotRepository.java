package com.example.ai_career_advisor.Repository.profile.core;

import com.example.ai_career_advisor.Entity.profile.core.PrfProfileSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필 스냅샷 리포지토리
 */
public interface PrfProfileSnapshotRepository extends JpaRepository<PrfProfileSnapshot, Long> {
}

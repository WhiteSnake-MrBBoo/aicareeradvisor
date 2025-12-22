package com.example.ai_career_advisor.Repository.profile.core;

import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필 루트 리포지토리
 */
public interface PrfProfileRepository extends JpaRepository<PrfProfile, Long> {

    List<PrfProfile> findByUserUserId(Long userId);
}

package com.example.ai_career_advisor.Repository.master;

import com.example.ai_career_advisor.Entity.master.MasWorkEnv;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 근무환경 마스터 리포지토리
 */
public interface MasWorkEnvRepository extends JpaRepository<MasWorkEnv, String> {
}

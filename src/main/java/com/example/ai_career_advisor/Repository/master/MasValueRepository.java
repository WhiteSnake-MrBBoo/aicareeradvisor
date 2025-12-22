package com.example.ai_career_advisor.Repository.master;

import com.example.ai_career_advisor.Entity.master.MasValue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 직업 가치관 마스터 리포지토리
 */
public interface MasValueRepository extends JpaRepository<MasValue, String> {
}

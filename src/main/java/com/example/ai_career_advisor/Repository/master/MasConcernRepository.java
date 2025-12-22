package com.example.ai_career_advisor.Repository.master;

import com.example.ai_career_advisor.Entity.master.MasConcern;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 진로 고민 마스터 리포지토리
 */
public interface MasConcernRepository extends JpaRepository<MasConcern, String> {
}

package com.example.ai_career_advisor.Repository.master;

import com.example.ai_career_advisor.Entity.master.MasSkill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 스킬 마스터 리포지토리
 */
public interface MasSkillRepository extends JpaRepository<MasSkill, String> {
}

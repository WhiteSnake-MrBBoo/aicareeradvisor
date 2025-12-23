package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileSkill;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileSkillId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-스킬 매핑 리포지토리
 * - mas_skill(스킬 마스터)와 프로필을 매핑합니다.
 * - 하나의 프로필에 여러 스킬이 연결됩니다.
 */
public interface PrfProfileSkillRepository
        extends JpaRepository<PrfProfileSkill, PrfProfileSkillId> {

    /**
     * 특정 프로필이 가진 모든 스킬 매핑 조회
     *  - profile.profileId = :profileId
     */
    List<PrfProfileSkill> findByProfile_ProfileId(Long profileId);
}


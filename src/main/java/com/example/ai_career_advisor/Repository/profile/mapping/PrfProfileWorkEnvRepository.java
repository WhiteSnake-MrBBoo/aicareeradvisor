package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileWorkEnv;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileWorkEnvId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-근무환경 매핑 리포지토리
 * - mas_work_env(선호 근무환경 마스터)와 프로필을 매핑합니다.
 */
public interface PrfProfileWorkEnvRepository
        extends JpaRepository<PrfProfileWorkEnv, PrfProfileWorkEnvId> {

    /**
     * 특정 프로필에 연결된 모든 선호 근무환경 매핑 조회
     *  - profile.profileId = :profileId
     */
    List<PrfProfileWorkEnv> findByProfile_ProfileId(Long profileId);
}



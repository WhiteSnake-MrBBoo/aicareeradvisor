package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileCareerPriority;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileCareerPriorityId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-진로 후보 우선순위 매핑 리포지토리
 * - 하나의 프로필에 대해, 1~N개의 진로 후보(직업 코드 등)와 우선순위를 매핑합니다.
 * - profile.profileId 기준으로 조회합니다.
 */
public interface PrfProfileCareerPriorityRepository
        extends JpaRepository<PrfProfileCareerPriority, PrfProfileCareerPriorityId> {

    /**
     * 특정 프로필에 연결된 진로 후보 목록을 우선순위 순으로 조회
     *  - profile.profileId = :profileId
     *  - ORDER BY priorityOrder ASC
     */
    List<PrfProfileCareerPriority> findByProfile_ProfileIdOrderByPriorityOrderAsc(Long profileId);
}

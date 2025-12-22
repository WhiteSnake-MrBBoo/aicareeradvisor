package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileValueRank;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileValueRankId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-가치관 랭킹 매핑 리포지토리
 */
public interface PrfProfileValueRankRepository extends JpaRepository<PrfProfileValueRank, PrfProfileValueRankId> {

    List<PrfProfileValueRank> findByProfileProfileId(Long profileId);
}

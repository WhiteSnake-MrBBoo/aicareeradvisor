package com.example.ai_career_advisor.Repository.profile.mapping;

import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileValueRank;
import com.example.ai_career_advisor.Entity.profile.mapping.PrfProfileValueRankId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로필-가치관 랭킹 매핑 리포지토리
 */
public interface PrfProfileValueRankRepository extends JpaRepository<PrfProfileValueRank, PrfProfileValueRankId> {

    //프로필 PK 가져 오기
    List<PrfProfileValueRank> findByProfileProfileId(Long profileId);

    //둘중에 아무거나 써도 됨 : 프로필을 최신 순으로 정렬래서 PK 가져 오는 부분임
    List<PrfProfileValueRank> findByProfile_ProfileIdOrderByRankOrderAsc(Long profileId);
}

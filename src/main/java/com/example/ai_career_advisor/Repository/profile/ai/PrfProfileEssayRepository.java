package com.example.ai_career_advisor.Repository.profile.ai;

import com.example.ai_career_advisor.Constant.YnType;
import com.example.ai_career_advisor.Entity.profile.ai.PrfProfileEssay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 프로필-자소서 버전 관리 리포지토리
 */
public interface PrfProfileEssayRepository extends JpaRepository<PrfProfileEssay, Long> {

    /** 특정 프로필의 모든 자소서 버전 목록 (최신 버전 순) */
    List<PrfProfileEssay> findByProfileProfileIdOrderByVersionNoDesc(Long profileId);

    /** 특정 프로필의 최신 버전 조회 */
    Optional<PrfProfileEssay> findFirstByProfileProfileIdOrderByVersionNoDesc(Long profileId);

    /** 특정 프로필의 대표 자소서 조회 (is_main = Y) */
    Optional<PrfProfileEssay> findFirstByProfileProfileIdAndIsMainOrderByVersionNoDesc(
            Long profileId,
            YnType isMain
    );

    /** 특정 프로필의 대표 자소서 조회 (is_main = Y)
     * findFirstByProfileProfileIdAndIsMainOrderByVersionNoDesc : 대체 내이티브 커리
     * */
//    @Query(value =
//            "SELECT * FROM prf_profile_essay " +
//                    "WHERE profile_id = :profileId " +
//                    "  AND is_main = :isMain " +
//                    "ORDER BY version_no DESC " +
//                    "LIMIT 1",
//            nativeQuery = true)
//    Optional<PrfProfileEssay> findLatestMainEssay(
//            @Param("profileId") Long profileId,
//            @Param("isMain") String isMain // YnType이 Enum이라면 .name() 등으로 전달 필요
//    );


    /** 특정 프로필의 모든 대표(Y) 자소서 목록 */
    List<PrfProfileEssay> findByProfileProfileIdAndIsMain(Long profileId, YnType isMain);

}

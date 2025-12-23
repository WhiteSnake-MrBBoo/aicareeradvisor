package com.example.ai_career_advisor.Entity.profile.mapping;

import com.example.ai_career_advisor.Entity.BaseEntity;
import com.example.ai_career_advisor.Entity.master.MasValue;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import jakarta.persistence.*;
import lombok.*;

/**
 * [prf_profile_value_rank] 프로필-가치관 랭킹 매핑 엔티티
 *
 * 목적
 * - "직업 가치관 Top3" 선택 및 순서를 저장합니다.
 * - 분석에서 가치관과 직무환경/직업군의 적합도를 계산하는 데 사용합니다.
 *
 * 제약
 * - (profile_id, rank_order) UNIQUE : 한 프로필에서 같은 순위는 1개만 허용
 *
 * Table: prf_profile_value_rank
 * PK   : (profile_id, value_cd)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "prf_profile_value_rank",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_prf_value_rank_profile_rank",
                        columnNames = {"profile_id", "rank_order"}
                )
        }
)
public class PrfProfileValueRank extends BaseEntity {

    /** 복합키 (profile_id + value_cd) */
    @EmbeddedId
    private PrfProfileValueRankId id;

    /** 프로필(FK) */
    @MapsId("profileId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_value_profile")
    )
    private PrfProfile profile;

    /** 가치관 마스터(FK) */
    @MapsId("valueCd")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "value_cd",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_value_master")
    )
    private MasValue value;


//    /**
//     * 가치관 우선순위 (1~3 등)
//     */
//    @Column(name = "priority_order")
//    private Integer priorityOrder;

    /** 우선순위(1~3) */
    @Column(name = "rank_order", nullable = false)
    private Integer rankOrder;
}

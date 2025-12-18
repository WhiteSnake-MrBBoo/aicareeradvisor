package com.example.ai_career_advisor.Entity.profile.mapping;

import com.example.ai_career_advisor.Entity.BaseEntity;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import jakarta.persistence.*;
import lombok.*;

/**
 * [prf_profile_career_priority] 프로필-진로 후보 우선순위 매핑 엔티티
 *
 * 목적
 * - 사용자가 고려 중인 진로 후보(직업군/직무군)와 우선순위를 저장합니다.
 * - 대표 목표 외의 대안 직무 추천/분석에 활용됩니다.
 *
 * 제약
 * - (profile_id, priority_order) UNIQUE : 한 프로필에서 같은 순위는 1개만 허용
 *
 * Table: prf_profile_career_priority
 * PK   : (profile_id, career_option_cd)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "prf_profile_career_priority",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_prf_priority_profile_order",
                        columnNames = {"profile_id", "priority_order"}
                )
        }
)
public class PrfProfileCareerPriority extends BaseEntity {

    /** 복합키 (profile_id + career_option_cd) */
    @EmbeddedId
    private PrfProfileCareerPriorityId id;

    /** 프로필(FK) */
    @MapsId("profileId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_priority_profile")
    )
    private PrfProfile profile;

    /** 진로 후보 코드(내부 직업군/직무군 코드) */
    @Column(name = "career_option_cd", length = 30, insertable = false, updatable = false)
    private String careerOptionCd;

    /** 우선순위(1~5) */
    @Column(name = "priority_order", nullable = false)
    private Integer priorityOrder;
}

package com.example.ai_career_advisor.Entity.profile.mapping;

import com.example.ai_career_advisor.Entity.BaseEntity;
import com.example.ai_career_advisor.Entity.master.MasConcern;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import jakarta.persistence.*;
import lombok.*;

/**
 * [prf_profile_concern] 프로필-진로 고민 매핑 엔티티
 *
 * 목적
 * - 진로 선택 시 사용자가 느끼는 고민/불안/제약 요인을 선택/기록합니다.
 * - 분석 결과에서 "리스크/케어 포인트" 및 액션 플랜 제안에 사용됩니다.
 *
 * Table: prf_profile_concern
 * PK   : (profile_id, concern_cd)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "prf_profile_concern")
public class PrfProfileConcern extends BaseEntity {

    /** 복합키 (profile_id + concern_cd) */
    @EmbeddedId
    private PrfProfileConcernId id;

    /** 프로필(FK) */
    @MapsId("profileId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_concern_profile")
    )
    private PrfProfile profile;

    /** 고민 마스터(FK) */
    @MapsId("concernCd")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "concern_cd",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_concern_master")
    )
    private MasConcern concern;

    /** 상세 설명(선택) */
    @Column(name = "detail_text", length = 500)
    private String detailText;
}

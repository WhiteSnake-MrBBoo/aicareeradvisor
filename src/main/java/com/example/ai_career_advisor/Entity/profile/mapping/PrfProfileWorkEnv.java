package com.example.ai_career_advisor.Entity.profile.mapping;

import com.example.ai_career_advisor.Entity.BaseEntity;
import com.example.ai_career_advisor.Entity.master.MasWorkEnv;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import jakarta.persistence.*;
import lombok.*;

/**
 * [prf_profile_work_env] 프로필-근무환경 매핑 엔티티
 *
 * 목적
 * - 사용자가 선호하는 근무환경(근무형태/복지 등)을 다중 선택으로 저장합니다.
 * - Work24 채용조건 매핑 및 분석 시 "환경 적합도" 계산에 사용합니다.
 *
 * Table: prf_profile_work_env
 * PK   : (profile_id, env_cd)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "prf_profile_work_env")
public class PrfProfileWorkEnv extends BaseEntity {

    /** 복합키 (profile_id + env_cd) */
    @EmbeddedId
    private PrfProfileWorkEnvId id;

    /** 프로필(FK) */
    @MapsId("profileId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_env_profile")
    )
    private PrfProfile profile;

    /** 근무환경 마스터(FK) */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("workEnvCd")
//    @JoinColumn(name = "work_env_cd")
//    private MasWorkEnv workEnv;

    // 개념 알고 체크 해야 할 사항 임 ..
    @MapsId("envCd")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "env_cd",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_env_master")
    )
    private MasWorkEnv env;

    /** 중요도(1~5, 선택) */
    @Column(name = "importance")
    private Integer importance;
}

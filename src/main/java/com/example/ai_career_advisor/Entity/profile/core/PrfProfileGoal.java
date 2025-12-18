package com.example.ai_career_advisor.Entity.profile.core;

import com.example.ai_career_advisor.Constant.TargetLevel;
import com.example.ai_career_advisor.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * [prf_profile_goal] 희망 커리어 방향(목표) 엔티티
 *
 * 목적
 * - 프로필의 "희망 직업군/직무/산업/목표 레벨"을 구조화하여 저장합니다.
 * - Phase 2(커리어 분석 V1)에서 추천 직업군 점수 산정의 핵심 입력으로 사용됩니다.
 *
 * 제약
 * - 프로필당 1개(1:1)를 권장합니다. (unique profile_id)
 *
 * Table: prf_profile_goal
 * PK   : goal_id
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "prf_profile_goal",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_prf_goal_profile_id", columnNames = "profile_id")
        }
)
public class PrfProfileGoal extends BaseEntity {

    /** 목표 식별자(PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    /**
     * 프로필(FK, 1:1)
     * - 한 프로필에 하나의 목표(희망 방향)를 둡니다.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_goal_profile")
    )
    private PrfProfile profile;

    /** 대표 직업군 코드(내부 표준 코드) */
    @Column(name = "target_job_group_cd", nullable = false, length = 30)
    private String targetJobGroupCd;

    /** 구체 직무명(예: 백엔드 개발자) */
    @Column(name = "target_role_title", length = 50)
    private String targetRoleTitle;

    /** 산업군 코드(내부 표준 코드) */
    @Column(name = "target_industry_cd", length = 30)
    private String targetIndustryCd;

    /** 목표 레벨(ENTRY/JUNIOR/MID/SENIOR) */
    @Enumerated(EnumType.STRING)
    @Column(name = "target_level", length = 20)
    private TargetLevel targetLevel;

    /** 목표 설명(동기/선호 등) */
    @Column(name = "goal_note", length = 500)
    private String goalNote;
}

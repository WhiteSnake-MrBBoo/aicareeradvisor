package com.example.ai_career_advisor.Entity.profile.mapping;

import com.example.ai_career_advisor.Constant.SkillLevel;
import com.example.ai_career_advisor.Entity.BaseEntity;
import com.example.ai_career_advisor.Entity.master.MasSkill;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import jakarta.persistence.*;
import lombok.*;

/**
 * [prf_profile_skill] 프로필-스킬 매핑 엔티티
 *
 * 목적
 * - 하나의 프로필이 여러 개의 스킬을 선택할 수 있도록 N:M 구조를 표현합니다.
 * - 분석 시 "보유 스킬 목록 및 숙련도"를 계산하는 핵심 소스입니다.
 *
 * Table: prf_profile_skill
 * PK   : (profile_id, skill_cd)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "prf_profile_skill",
        indexes = {
                @Index(name = "idx_prf_skill_profile_id", columnList = "profile_id")
        }
)
public class PrfProfileSkill extends BaseEntity {

    /** 복합키 (profile_id + skill_cd) */
    @EmbeddedId
    private PrfProfileSkillId id;

    /**
     * 프로필 엔티티
     * - @MapsId("profileId")로 복합키의 profileId 필드를 이 관계에 매핑합니다.
     */
    @MapsId("profileId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_skill_profile")
    )
    private PrfProfile profile;

    /**
     * 스킬 마스터 엔티티
     * - @MapsId("skillCd")로 복합키의 skillCd 필드를 이 관계에 매핑합니다.
     */
    @MapsId("skillCd")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "skill_cd",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_skill_master")
    )
    private MasSkill skill;

    /** 스킬 숙련도(BASIC/INTERMEDIATE/ADVANCED), 선택 입력 */
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_level", length = 20)
    private SkillLevel skillLevel;

    /** 사용 기간(년 단위, 선택 입력) */
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
}

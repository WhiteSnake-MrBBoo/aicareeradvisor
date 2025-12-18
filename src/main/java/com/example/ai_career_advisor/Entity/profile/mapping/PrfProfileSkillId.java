package com.example.ai_career_advisor.Entity.profile.mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.*;

/**
 * [prf_profile_skill] 복합키 ID
 * - profile_id + skill_cd 조합으로 1개의 레코드를 식별합니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PrfProfileSkillId implements Serializable {

    /** 프로필 ID(PK 일부) */
    @Column(name = "profile_id")
    private Long profileId;

    /** 스킬 코드(PK 일부) */
    @Column(name = "skill_cd", length = 30)
    private String skillCd;
}

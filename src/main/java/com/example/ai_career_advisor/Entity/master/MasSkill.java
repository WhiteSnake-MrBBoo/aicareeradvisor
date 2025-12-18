package com.example.ai_career_advisor.Entity.master;

import com.example.ai_career_advisor.Constant.YnType;
import jakarta.persistence.*;
import lombok.*;

/**
 * [mas_skill] 스킬 마스터
 * - 프로필 스킬 선택은 반드시 이 마스터를 기준으로만 이루어집니다(자유입력 금지).
 *
 * Table: mas_skill
 * PK   : skill_cd
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mas_skill")
public class MasSkill {

    /** 스킬 코드(PK) - 내부 표준 코드 */
    @Id
    @Column(name = "skill_cd", length = 30)
    private String skillCd;

    /** 스킬 이름(화면 노출명) */
    @Column(name = "skill_name", nullable = false, length = 50)
    private String skillName;

    /** 정렬 순서(작을수록 우선 노출) */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    /** 활성 여부(Y/N) */
    @Enumerated(EnumType.STRING)
    @Column(name = "is_active", nullable = false, length = 1)
    private YnType isActive;

    @PrePersist
    public void prePersist() {
        // 기본값 세팅(DDL의 DEFAULT와 같은 의미를 코드에서도 보장)
        if (sortOrder == null) {
            Integer defaultSortOrder = 0;
            sortOrder = defaultSortOrder;
        }
        if (isActive == null) {
            YnType defaultActive = YnType.Y;
            isActive = defaultActive;
        }
    }
}

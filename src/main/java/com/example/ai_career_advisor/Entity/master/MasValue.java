package com.example.ai_career_advisor.Entity.master;

import com.example.ai_career_advisor.Constant.YnType;
import jakarta.persistence.*;
import lombok.*;

/**
 * [mas_value] 직업 가치관 마스터
 * - 프로필에서 Top3 가치관 선택/순위(rank) 입력에 사용됩니다.
 *
 * Table: mas_value
 * PK   : value_cd
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mas_value")
public class MasValue {

    /** 가치관 코드(PK) */
    @Id
    @Column(name = "value_cd", length = 30)
    private String valueCd;

    /** 가치관 이름(화면 노출명) */
    @Column(name = "value_name", nullable = false, length = 50)
    private String valueName;

    /** 정렬 순서 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    /** 활성 여부(Y/N) */
    @Enumerated(EnumType.STRING)
    @Column(name = "is_active", nullable = false, length = 1)
    private YnType isActive;

    @PrePersist
    public void prePersist() {
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

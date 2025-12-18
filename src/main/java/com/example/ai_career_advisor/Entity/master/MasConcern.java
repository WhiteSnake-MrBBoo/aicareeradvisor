package com.example.ai_career_advisor.Entity.master;

import com.example.ai_career_advisor.Constant.YnType;
import jakarta.persistence.*;
import lombok.*;

/**
 * [mas_concern] 진로 고민 마스터
 * - 프로필의 '진로 선택 시 고민되는 부분' 선택 항목에 사용됩니다.
 *
 * Table: mas_concern
 * PK   : concern_cd
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mas_concern")
public class MasConcern {

    /** 고민 코드(PK) */
    @Id
    @Column(name = "concern_cd", length = 30)
    private String concernCd;

    /** 고민 이름(화면 노출명) */
    @Column(name = "concern_name", nullable = false, length = 80)
    private String concernName;

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

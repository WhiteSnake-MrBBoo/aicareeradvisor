package com.example.ai_career_advisor.Entity.master;

import com.example.ai_career_advisor.Constant.YnType;
import jakarta.persistence.*;
import lombok.*;

/**
 * [mas_work_env] 근무환경 마스터
 * - 프로필의 '선호 근무 환경' 선택 항목에 사용됩니다.
 *
 * Table: mas_work_env
 * PK   : env_cd
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mas_work_env")
public class MasWorkEnv {

    /** 근무환경 코드(PK) */
    @Id
    @Column(name = "env_cd", length = 30)
    private String envCd;

    /** 근무환경 이름(화면 노출명) */
    @Column(name = "env_name", nullable = false, length = 50)
    private String envName;

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

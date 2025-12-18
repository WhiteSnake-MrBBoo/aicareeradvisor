package com.example.ai_career_advisor.Entity.profile.core;

import com.example.ai_career_advisor.Constant.ProfileType;
import com.example.ai_career_advisor.Entity.BaseEntity;
import com.example.ai_career_advisor.Entity.user.AppUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * [prf_profile] 프로필 루트(부모) 엔티티
 *
 * 목적
 * - 서비스가 요구하는 "고유 양식"의 프로필 입력을 표준화하여 저장합니다.
 * - 하위(자식) 테이블들(스킬/경험/가치관/우선순위/환경/고민)이 이 테이블을 참조합니다.
 *
 * 연관
 * - AppUser(사용자) : N:1 (한 사용자는 여러 프로필을 가질 수 있음)
 *
 * Table: prf_profile
 * PK   : profile_id
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "prf_profile",
        indexes = {
                @Index(name = "idx_prf_profile_user_id", columnList = "user_id")
        }
)
public class PrfProfile extends BaseEntity {

    /** 프로필 식별자(PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    /**
     * 사용자(FK)
     * - 테스트 단계에서는 Security 없이도 user_id만으로 식별/연결합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_profile_user")
    )
    private AppUser user;

    /**
     * 프로필 타입
     * - 입력 폼 노출 규칙, 분석 분기 등에서 핵심 키로 활용됩니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type", nullable = false, length = 30)
    private ProfileType profileType;

    /** 프로필 제목(사용자 식별/대시보드 표기용) */
    @Column(name = "profile_title", length = 50)
    private String profileTitle;

    /**
     * 프로필 완성도(0~100)
     * - 서버에서 규칙 기반으로 산정하여 업데이트합니다.
     */
    @Column(name = "progress_rate", nullable = false)
    private Integer progressRate;

    /**
     * 프로필 작성 완료(또는 마지막 완료 처리) 시각
     * - 대시보드 요약/최근 업데이트 기준에 사용합니다.
     */
    @Column(name = "last_completed_at")
    private LocalDateTime lastCompletedAt;

    @PrePersist
    public void prePersist() {
        // progressRate 기본값을 코드에서도 보장(DDL DEFAULT와 동일한 안정성 확보)
        if (progressRate == null) {
            Integer defaultProgressRate = 0;
            progressRate = defaultProgressRate;
        }
    }
}

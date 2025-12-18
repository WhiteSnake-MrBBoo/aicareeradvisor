package com.example.ai_career_advisor.Entity.profile.core;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

/**
 * [prf_profile_snapshot] 프로필 스냅샷(버전 고정) 엔티티
 *
 * 목적
 * - 분석/자기소개서 생성 시점의 프로필 상태를 "불변 입력"으로 고정합니다.
 * - Phase 2/자소서 기능에서 재현성(같은 입력 → 같은 결과/근거)을 확보합니다.
 *
 * 제약
 * - (profile_id, snapshot_version) 유니크
 *
 * Table: prf_profile_snapshot
 * PK   : snapshot_id
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "prf_profile_snapshot",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_prf_snapshot_profile_version",
                        columnNames = {"profile_id", "snapshot_version"}
                )
        },
        indexes = {
                @Index(name = "idx_prf_snapshot_profile_id", columnList = "profile_id")
        }
)
public class PrfProfileSnapshot {

    /** 스냅샷 식별자(PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snapshot_id")
    private Long snapshotId;

    /** 대상 프로필(FK) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_snapshot_profile")
    )
    private PrfProfile profile;

    /**
     * 스냅샷 버전(1부터 증가)
     * - 분석 실행 시점에 현재 최신 버전 + 1 로 생성하는 것을 권장합니다.
     */
    @Column(name = "snapshot_version", nullable = false)
    private Integer snapshotVersion;

    /**
     * 스냅샷 본문(JSON)
     * - 프로필의 입력값을 서버에서 조합하여 JSON으로 저장합니다.
     * - 분석/자소서 모듈은 이 JSON을 표준 입력으로 사용합니다.
     */
    @Lob
    @Column(name = "snapshot_payload_json", nullable = false, columnDefinition = "LONGTEXT")
    private String snapshotPayloadJson;

    /** 생성일시(스냅샷 생성 시각) */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime regDate;

    @PrePersist
    public void prePersist() {
        if (regDate == null) {
            LocalDateTime nowValue = LocalDateTime.now();
            regDate = nowValue;
        }
    }
}

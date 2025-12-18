package com.example.ai_career_advisor.Entity.profile.experience;

import com.example.ai_career_advisor.Constant.ExperienceType;
import com.example.ai_career_advisor.Entity.BaseEntity;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

/**
 * [prf_profile_experience] 프로필 경험(1:N) 엔티티
 *
 * 목적
 * - 경력/프로젝트/교육/자격/활동 등 다양한 경험 데이터를 표준화하여 저장합니다.
 * - 커리어 분석 V1에서 "근거(왜 추천인가)" 생성 시 가장 중요한 소스입니다.
 *
 * Table: prf_profile_experience
 * PK   : experience_id
 * FK   : profile_id -> prf_profile.profile_id
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "prf_profile_experience",
        indexes = {
                @Index(name = "idx_prf_exp_profile_id", columnList = "profile_id")
        }
)
public class PrfProfileExperience extends BaseEntity {

    /** 경험 식별자(PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private Long experienceId;

    /** 대상 프로필(FK) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_exp_profile")
    )
    private PrfProfile profile;

    /** 경험 유형(WORK/PROJECT/EDU/CERT/ACTIVITY) */
    @Enumerated(EnumType.STRING)
    @Column(name = "experience_type", nullable = false, length = 20)
    private ExperienceType experienceType;

    /** 경험 제목(예: HexaStay Admin 개발) */
    @Column(name = "title", nullable = false, length = 80)
    private String title;

    /** 기관/회사/팀명(선택) */
    @Column(name = "organization", length = 80)
    private String organization;

    /** 시작일(선택) */
    @Column(name = "start_date")
    private LocalDate startDate;

    /** 종료일(선택) */
    @Column(name = "end_date")
    private LocalDate endDate;

    /** 상세 설명(무엇을 했는지) */
    @Column(name = "description", nullable = false, length = 800)
    private String description;

    /** 성과/산출물(선택) */
    @Column(name = "outcome", length = 400)
    private String outcome;

    /** 링크(깃허브/포트폴리오/문서) */
    @Column(name = "link_url", length = 200)
    private String linkUrl;
}

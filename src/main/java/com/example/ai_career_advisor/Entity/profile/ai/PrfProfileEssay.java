package com.example.ai_career_advisor.Entity.profile.ai;

import com.example.ai_career_advisor.Constant.AiScenario;
import com.example.ai_career_advisor.Constant.YnType;
import com.example.ai_career_advisor.Entity.BaseEntity;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import jakarta.persistence.*;
import lombok.*;

/**
 * [prf_profile_essay] 프로필별 자기소개서 버전 관리 엔티티
 *
 * - 한 프로필에 여러 개의 자소서 버전을 저장합니다.
 * - (profile_id, version_no) 조합으로 버전 순서를 관리하고,
 *   is_main = Y 인 버전을 "대표 자소서"로 사용할 수 있습니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "prf_profile_essay",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_prf_essay_profile_version",
                        columnNames = {"profile_id", "version_no"}
                )
        }
)
public class PrfProfileEssay extends BaseEntity {

    /** 자소서 PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "essay_id")
    private Long essayId;

    /** 프로필(FK) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prf_essay_profile")
    )
    private PrfProfile profile;

    /** AI 시나리오 (현재는 ESSAY 고정) */
    @Enumerated(EnumType.STRING)
    @Column(name = "scenario", nullable = false, length = 30)
    private AiScenario scenario;

    /** 프로필별 버전 번호 (1,2,3...) */
    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    /** 대표 자소서 여부 (Y/N) */
    @Enumerated(EnumType.STRING)
    @Column(name = "is_main", nullable = false, length = 1)
    private YnType isMain;

    /** 자소서 제목(선택) */
    @Column(name = "title", length = 200)
    private String title;

    /** 자소서 전체 내용 */
    
    /**
     * columnDefinition = "LONGTEXT":
     * JPA가 스키마를 생성/변경할 때 MariaDB 기준 LONGTEXT 로 박도록 명시
     * */
    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    /** 사용한 모델명(OpenAI 등) */
    @Column(name = "model_name", length = 100)
    private String modelName;

    /** 프롬프트 템플릿 키(AiScenario.promptKey 등) */
    @Column(name = "prompt_key", length = 50)
    private String promptKey;
}

package com.example.ai_career_advisor.Entity.user;

import com.example.ai_career_advisor.Constant.UserGroup;
import com.example.ai_career_advisor.Entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * [app_user] 테스트 단계 사용자 엔티티
 * - Spring Security 적용 전 단계에서 "로그인 대체(식별)" 목적으로 사용합니다.
 * - 실제 인증/권한은 추후 Security 도입 시 별도 확장합니다.
 *
 * Table: app_user
 * PK   : user_id
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_app_user_email", columnNames = "email")
        }
)
public class AppUser extends BaseEntity {

    /** 사용자 식별자(PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /** 화면 표시용 이름(닉네임/실명) */
    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;

    /**
     * 이메일
     * - 테스트 로그인에서 사용자가 선택/생성하는 키로 활용 가능
     */
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    /**
     * 사용자 그룹(상위 분류)
     * - ADULT(성인) / YOUTH(청소년) - 유저 그룹 분류 Enum
     * - 프로필 타입(고등학생/대학생 등)과 별개
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_group", nullable = false, length = 20)
    private UserGroup userGroup;
}

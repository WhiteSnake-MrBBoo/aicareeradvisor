package com.example.ai_career_advisor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 설정
 * - BaseTimeEntity의 createdAt/updatedAt 컬럼을 자동으로 채우기 위해 사용합니다.
 * - Entity에 @CreatedDate, @LastModifiedDate가 적용되어 있을 때 동작합니다.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // 별도 Bean 정의가 필요하지 않은 기본 설정입니다.
}

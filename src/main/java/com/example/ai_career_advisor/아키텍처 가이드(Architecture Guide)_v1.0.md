# 📋 Project Design Blueprint: AI Career Advisor (v1.0)

이 문서는 프로젝트의 명명 규칙, 패키지 구조 및 주요 클래스 구성안을 정의합니다. 모든 개발은 본 설계안을 기준으로 진행합니다.

---

## A) 네이밍 컨벤션 (Naming Convention)

### 1. DB 테이블 Prefix
* **`app_`**: 사용자 및 시스템 공통 영역 (예: 회원, 공통 설정)
* **`prf_`**: 프로필 도메인 영역 (핵심 서비스 데이터)
* **`mas_`**: 마스터 코드 데이터 (스킬, 가치관, 근무환경 등 기준 데이터)

### 2. Entity 클래스명 규칙
* **규칙**: 테이블명(snake_case) → PascalCase 변환
* **예시**:
    * `app_user` → `AppUser`
    * `prf_profile_skill` → `PrfProfileSkill`
    * `mas_skill` → `MasSkill`
* **목적**: DB/ERD와 코드 간의 추적성을 극대화하면서 Java 관례 준수

### 3. 부모-자식 관계 표현
* 자식 테이블은 부모의 Prefix를 포함하여 계층 구조가 드러나도록 명명 (`prf_profile_*` 형태)

---

## B) 패키지 트리 구조 (v1.0)

```text
com.example.ai_career_advisor
├─ Config (설정 클래스)
├─ Constant (Enum 및 상수)
├─ Controller (API 엔드포인트)
│  ├─ user
│  └─ profile
├─ DTO (Request/Response 객체)
│  ├─ user
│  └─ profile
│     ├─ request
│     └─ response
├─ Entity (JPA 엔티티)
│  ├─ user
│  ├─ master
│  └─ profile
│     ├─ core (루트/골/스냅샷)
│     ├─ mapping (N:M 매핑 테이블)
│     └─ experience (경험 이력)
├─ Mapper (Entity-DTO 변환)
├─ Repository (Data Access)
│  ├─ user
│  ├─ master
│  └─ profile (core/mapping/experience 하위 분리)
├─ Service (비즈니스 인터페이스)
│  ├─ user
│  └─ profile
├─ ServiceImpl (비즈니스 구현체)
│  ├─ user
│  └─ profile
└─ Util (공통 유틸리티)
```
---
# 📋 Project Design Blueprint: AI Career Advisor (v1.0)

## C) 주요 클래스/파일명 목록

### 1. Config & Constant
* **Config**:
    * `JpaAuditingConfig` (생성/수정일 자동화)
    * `ModelMapperConfig` (객체 매핑 설정)
    * `WebMvcConfig` (MVC 설정)
* **Enum (Constant)**:
    * `UserGroup`, `ProfileType`, `ExperienceType`, `SkillLevel`, `TargetLevel`, `YnType`

### 2. Entity & Repository (도메인별)
* **User 도메인**:
    * `AppUser` / `AppUserRepository`
* **Master 도메인**:
    * `MasSkill`, `MasValue`, `MasWorkEnv`, `MasConcern`
    * 각 엔티티별 `Repository` 존재
* **Profile Core**:
    * `PrfProfile`, `PrfProfileGoal`, `PrfProfileSnapshot`
* **Profile Experience**:
    * `PrfProfileExperience`
* **Profile Mapping (N:M 및 랭킹)**:
    * **엔티티**: `PrfProfileSkill`, `PrfProfileValueRank`, `PrfProfileCareerPriority`, `PrfProfileWorkEnv`, `PrfProfileConcern`
    * **복합키(IdClass/EmbeddedId)**: `PrfProfileSkillId`, `PrfProfileValueRankId`, `PrfProfileCareerPriorityId`, `PrfProfileWorkEnvId`, `PrfProfileConcernId`

---

## D) 개발 및 작업 규칙

### 1. 주석 작성 (Documentation)
* **Javadoc 필수**: 모든 클래스 상단에 해당 클래스의 역할, 관련 도메인, 연관 DB 테이블, 핵심 제약조건(Unique 등)을 명시합니다.
* **업무 의미 중심**: 필드 주석 작성 시 단순히 "이름 정보"라고 적는 것이 아니라, "사용자가 서비스에서 사용하는 실명(Unique 성격 포함)"과 같이 **비즈니스적 의미**를 담아 작성합니다.

### 2. 코드 구현 규칙
* **변수 할당 후 반환 (Local Variable Return)**: 디버깅 편의성과 가독성을 위해 모든 메서드의 반환값은 반드시 지역 변수에 먼저 할당한 후 `return` 합니다.

  ```java
  // Good Example
  public UserResponseDTO getUser(Long id) {
      UserResponseDTO response = userRepository.findById(id)
          .map(userMapper::toDto)
          .orElseThrow(() -> new EntityNotFoundException("User not found"));
          
      return response;
  }
  ```
 ---
## F) BaseEntity 정의

### 생성일자 / 수정 일자 : [변수명 정의]
- [생성일자] regDate
- [수정일자] modDate
```java
package com.example.ai_career_advisor.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass //연결용
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    
    //생성일자
    @Column(name="regDate", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime regDate;

    //수정일자
    @Column(name = "modDate")
    @LastModifiedDate
    private LocalDateTime modDate;
}

```



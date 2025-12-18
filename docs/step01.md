### Step 1. 필드 정의서 v0.2
> 0) 테스트용 “회원(로그인 대체)” 최소 테이블
>> 인증이 아니라 식별 목적. 테스트에서는 세션에 user_id만 유지.
---
<div style="height: 2px; background: #ffc800;"></div>

- ### app_user

| 필드키          | 라벨     |           타입 |  필수 | 검증/제약              | 비고                 |
| ------------ | ------ | -----------: | :-: | ------------------ | ------------------ |
| user_id      | 사용자 ID |           PK |  Y  | BIGINT(권장) 또는 UUID | 모든 모듈의 기준 키        |
| display_name | 이름(표시) |  VARCHAR(50) |  Y  | 1~50               | 화면 표시용             |
| email        | 이메일    | VARCHAR(100) |  Y  | UNIQUE, 이메일 형식     | 테스트 로그인 키로 사용 가능   |
| user_group   | 사용자군   |  VARCHAR(20) |  Y  | `ADULT`,`YOUTH`    | 프로필 타입과는 별도(상위 분류) |
| created_at   | 생성일    |     DATETIME |  Y  |                    |                    |
| updated_at   | 수정일    |     DATETIME |  Y  |                    |                    |

- ## 1) 프로필 루트(공통)
- career_profile

| 필드키                     | 라벨     |          타입 |  필수 | 검증/제약                                                                   | 분석 사용 |
| ----------------------- | ------ | ----------: | :-: | ----------------------------------------------------------------------- | :---: |
| profile_id              | 프로필 ID |          PK |  Y  |                                                                         |   -   |
| user_id                 | 사용자 ID |          FK |  Y  | app_user.user_id                                                        |   Y   |
| profile_type            | 프로필 타입 | VARCHAR(30) |  Y  | `ADULT_JOBSEEKER`,`HIGHSCHOOL`,`MIDDLESCHOOL`,`ELEMENTARY`,`UNIVERSITY` |   Y   |
| profile_title           | 프로필 제목 | VARCHAR(50) |  N  | 0~50                                                                    |   N   |
| progress_rate           | 완성도(%) |         INT |  Y  | 0~100 (서버 계산)                                                           |   N   |
| last_completed_at       | 최근 완료일 |    DATETIME |  N  |                                                                         |   N   |
| created_at / updated_at | 생성/수정일 |    DATETIME |  Y  |                                                                         |   N   |

<div style="height: 2px; background: red;"></div>
<div style="height: 3px; background: linear-gradient(to right, red, yellow, green);"></div>

---

- ## 2) 입력 섹션별 정의 (a~g)
- ### a) 이용 고객 기본 설정(프로필 설정)
> 로그인정보 자체는 app_user, 프로필 설정은 career_profile에서 관리

| 필드키                        | 라벨     | UI 타입  |  필수 | 검증/제약        | 비고         |
| -------------------------- | ------ | ------ | :-: | ------------ | ---------- |
| profile_type               | 대상 유형  | select |  Y  | 고정 옵션        | 화면 진입 시 필수 |
| profile_title              | 프로필 제목 | text   |  N  | 0~50         | 대시보드 표기용   |
| (옵션) preferred_region_code | 선호 지역  | select |  N  | master(code) | 성인에서 유용    |
---
- ## b) 희망 커리어 방향
> - ### 권장 테이블: career_goal

| 필드키                   | 라벨     | UI 타입    |  필수 | 검증/제약                     |  분석 사용 |
| --------------------- | ------ | -------- | :-: | ------------------------- | :----: |
| target_job_group_code | 대표 직업군 | select   |  Y  | master(code)              |    Y   |
| target_role_title     | 희망 직무명 | text     |  N  | 0~50                      |    Y   |
| target_industry_code  | 희망 산업군 | select   |  N  | master(code)              |    Y   |
| target_level          | 목표 레벨  | select   |  N  | `ENTRY/JUNIOR/MID/SENIOR` |    Y   |
| goal_note             | 목표 설명  | textarea |  N  | 0~500                     | N(서술용) |
---
- ## c) 보유 역량 및 경험 (스킬 고정)
- ### c-1) 스킬(고정 마스터 기반)
> - ### 권장 테이블: profile_skill

| 필드키                 | 라벨       | UI 타입        |  필수 | 검증/제약                         | 분석 사용 |
| ------------------- | -------- | ------------ | :-: | ----------------------------- | :---: |
| skill_code          | 보유 스킬    | multi-select |  Y  | master(code), 최소 1            |   Y   |
| skill_level         | 숙련도      | select       |  N  | `BASIC/INTERMEDIATE/ADVANCED` |   Y   |
| years_of_experience | 사용 기간(년) | number       |  N  | 0~50                          |   Y   |

- ### c-2) 경험/활동/프로젝트
> - ### 권장 테이블: profile_experience

| 필드키                   | 라벨    | UI 타입    |  필수 | 검증/제약                            | 분석 사용 |
| --------------------- | ----- | -------- | :-: | -------------------------------- | :---: |
| experience_type       | 유형    | select   |  Y  | `WORK/PROJECT/EDU/CERT/ACTIVITY` |   Y   |
| title                 | 제목    | text     |  Y  | 1~80                             |   Y   |
| organization          | 기관/회사 | text     |  N  | 0~80                             |   N   |
| start_date / end_date | 기간    | date     |  N  | end>=start                       | Y(기간) |
| description           | 내용    | textarea |  Y  | 1~800                            |   Y   |
| outcome               | 성과    | textarea |  N  | 0~400                            | Y(가점) |
| link_url              | 링크    | text     |  N  | 0~200                            |   N   |

---
- ## d) 직업 가치관(고정 레퍼런스)
> - ### 권장 테이블: profile_value_rank
| 필드키        | 라벨     | UI 타입       |  필수 | 검증/제약                    | 분석 사용 |
| ---------- | ------ | ----------- | :-: | ------------------------ | :---: |
| value_code | 가치관 항목 | rank-select |  Y  | master(code), Top3 고정 권장 |   Y   |
| rank_order | 우선순위   | rank        |  Y  | 1~3                      |   Y   |

---
- ## e) 진로 가능성(우선순위 선택)
- ### “고려 중인 진로 후보 리스트”를 분리 저장 (분석에서 대안 추천/분기 처리에 사용)
> - ### 권장 테이블: profile_career_priority
| 필드키                | 라벨    | UI 타입             |  필수 | 검증/제약                   | 분석 사용 |
| ------------------ | ----- | ----------------- | :-: | ----------------------- | :---: |
| career_option_code | 진로 후보 | rank-multi-select |  Y  | master(code), 최소 1~최대 5 |   Y   |
| priority_order     | 우선순위  | rank              |  Y  | 1~5                     |   Y   |

---
- ## f) 선호 근무 환경(실무 조건)
> - ### 권장 테이블: profile_work_env

| 필드키        | 라벨      | UI 타입        |  필수 | 검증/제약              |  분석 사용 |
| ---------- | ------- | ------------ | :-: | ------------------ | :----: |
| env_code   | 근무환경 항목 | multi-select |  Y  | master(code), 최소 1 |    Y   |
| importance | 중요도     | select       |  N  | 1~5                | Y(가중치) |

- (선택 확장: 구조화 필드)
- - work_mode(ONSITE/HYBRID/REMOTE)
- - company_size(STARTUP/SME/ENTERPRISE/PUBLIC)
- - overtime_tolerance(LOW/MID/HIGH)
---

- ## g) 진로 선택 시 고민되는 부분
> - ### 권장 테이블: profile_concern

| 필드키          | 라벨    | UI 타입        |  필수 | 검증/제약        |   분석 사용   |
| ------------ | ----- | ------------ | :-: | ------------ | :-------: |
| concern_code | 고민 항목 | multi-select |  N  | master(code) | Y(리스크/액션) |
| detail_text  | 추가 설명 | textarea     |  N  | 0~500        |   Y(서술)   |

---
<div style="height: 2px; background: red;"></div>
<div style="height: 3px; background: linear-gradient(to right, red, yellow, green);"></div>

# 4) 프로필 스냅샷(버전 고정) — 분석/자소서/검사 연동의 기준
>- ## Phase 2부터 “분석 실행 시점의 입력 상태”를 고정하기 위한 장치입니다.
>> - ### profile_snapshot

| 필드키                   | 라벨     |            타입 |  필수 | 검증/제약                     | 비고            |
| --------------------- | ------ | ------------: | :-: | ------------------------- | ------------- |
| snapshot_id           | 스냅샷 ID |            PK |  Y  |                           | 분석/자소서 요청에 사용 |
| profile_id            | 프로필 ID |            FK |  Y  | career_profile.profile_id |               |
| snapshot_version      | 버전     |           INT |  Y  | 1부터 증가                    |               |
| snapshot_payload_json | 스냅샷 본문 | JSON/LONGTEXT |  Y  | 서버 생성                     | 당시 입력을 그대로 저장 |
| created_at            | 생성일    |      DATETIME |  Y  |                           |               |


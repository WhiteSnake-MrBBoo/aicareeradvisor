package com.example.ai_career_advisor.Repository.user;

import com.example.ai_career_advisor.Entity.user.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 리포지토리
 * - 이메일 기반 조회(테스트용 로그인/식별)에 사용합니다.
 */

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);
}

package com.example.ai_career_advisor.Service.user;

import com.example.ai_career_advisor.Constant.UserGroup;
import com.example.ai_career_advisor.Entity.user.AppUser;
import java.util.List;
import java.util.Optional;

/**
 * 사용자 도메인 서비스
 * - 테스트 단계에서는 이메일 기반 "조회 또는 생성"과
 *   사용자 리스트 조회 기능을 제공합니다.
 */
public interface UserService {

    /**
     * 이메일 기준으로 사용자 조회 후,
     * 없으면 새로 생성하여 반환합니다.
     */
    AppUser getOrCreateUser(String displayName, String email, UserGroup userGroup);

    /**
     * 모든 사용자 리스트를 반환합니다.
     */
    List<AppUser> getAllUsers();

    /**
     * 사용자 ID 기준으로 단일 사용자 조회를 시도합니다.
     */
    Optional<AppUser> findById(Long userId);
}

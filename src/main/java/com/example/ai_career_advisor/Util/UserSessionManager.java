package com.example.ai_career_advisor.Util;

import com.example.ai_career_advisor.Constant.UserGroup;
import com.example.ai_career_advisor.Entity.user.AppUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

/**
 * 현재 선택된 사용자 정보를 세션에 보관/조회하는 유틸 컴포넌트
 *
 * TODO: 추후 Spring Security 도입 시
 *       - HttpSession 기반이 아니라 SecurityContext의 Principal 기반으로
 *         구현을 교체할 예정입니다.
 */
@Component
public class UserSessionManager {

    private static final String KEY_CURRENT_USER_ID = "CURRENT_USER_ID";
    private static final String KEY_CURRENT_USER_NAME = "CURRENT_USER_NAME";
    private static final String KEY_CURRENT_USER_GROUP = "CURRENT_USER_GROUP";

    /**
     * 현재 선택된 사용자를 세션에 저장합니다.
     */
    public void setCurrentUser(HttpSession session, AppUser user) {

        Long userId = user.getUserId();
        String userName = user.getDisplayName();
        UserGroup userGroup = user.getUserGroup();

        session.setAttribute(KEY_CURRENT_USER_ID, userId);
        session.setAttribute(KEY_CURRENT_USER_NAME, userName);
        session.setAttribute(KEY_CURRENT_USER_GROUP, userGroup);
    }

    /**
     * 세션에서 현재 사용자 ID를 조회합니다.
     */
    public Long getCurrentUserId(HttpSession session) {

        Object attribute = session.getAttribute(KEY_CURRENT_USER_ID);
        if (attribute instanceof Long) {
            Long userId = (Long) attribute;
            return userId;
        }

        Long userId = null;
        return userId;
    }

    /**
     * 세션에서 현재 사용자 이름을 조회합니다.
     */
    public String getCurrentUserName(HttpSession session) {

        Object attribute = session.getAttribute(KEY_CURRENT_USER_NAME);
        if (attribute instanceof String) {
            String name = (String) attribute;
            return name;
        }

        String name = null;
        return name;
    }

    /**
     * 세션에서 현재 사용자 그룹을 조회합니다.
     */
    public UserGroup getCurrentUserGroup(HttpSession session) {

        Object attribute = session.getAttribute(KEY_CURRENT_USER_GROUP);
        if (attribute instanceof UserGroup) {
            UserGroup group = (UserGroup) attribute;
            return group;
        }

        UserGroup group = null;
        return group;
    }

    /**
     * 현재 사용자 관련 세션 정보를 초기화합니다.
     */
    public void clearCurrentUser(HttpSession session) {

        session.removeAttribute(KEY_CURRENT_USER_ID);
        session.removeAttribute(KEY_CURRENT_USER_NAME);
        session.removeAttribute(KEY_CURRENT_USER_GROUP);
    }
}

package com.example.ai_career_advisor.Controller.user;

import com.example.ai_career_advisor.DTO.user.UserCreateForm;
import com.example.ai_career_advisor.Entity.user.AppUser;
import com.example.ai_career_advisor.Service.user.UserService;
import com.example.ai_career_advisor.Util.UserSessionManager;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 선택/생성 View 컨트롤러
 *
 * 기능
 * - 기존 사용자 리스트 조회
 * - 신규 사용자 생성
 * - 특정 사용자를 "현재 세션의 사용자"로 선택
 *
 * 참고:
 * - 현 단계에서는 Spring Security/Principal 없이 HttpSession을 사용합니다.
 * - TODO: 추후 Security 도입 시 UserSessionManager 내부 구현을 교체합니다.
 */
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;
    private final UserSessionManager userSessionManager;

    /**
     * 사용자 선택/생성 페이지
     * - 좌측: 기존 사용자 리스트
     * - 우측: 신규 사용자 생성 폼
     */
    @GetMapping("/select")
    public String showUserSelectPage(HttpSession session, Model model) {

        List<AppUser> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);

        UserCreateForm createForm = new UserCreateForm();
        model.addAttribute("userCreateForm", createForm);

        Long currentUserId = userSessionManager.getCurrentUserId(session);
        String currentUserName = userSessionManager.getCurrentUserName(session);

        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("currentUserName", currentUserName);

        String viewName = "user/user-select";
        return viewName;
    }

    /**
     * 신규 사용자 생성 후, 해당 사용자를 현재 세션 사용자로 설정합니다.
     */
    @PostMapping("/create")
    public String createUserAndSelect(
            HttpSession session,
            @ModelAttribute("userCreateForm") UserCreateForm form
    ) {

        AppUser user = userService.getOrCreateUser(
                form.getDisplayName(),
                form.getEmail(),
                form.getUserGroup()
        );

        userSessionManager.setCurrentUser(session, user);

        // TODO: 추후에는 프로필 목록 또는 대시보드로 이동하는 것이 자연스럽습니다.
        String redirectPath = "redirect:/user/select";
        return redirectPath;
    }

    /**
     * 기존 사용자 중 하나를 선택하여,
     * 현재 세션 사용자로 설정합니다.
     */
    @PostMapping("/select")
    public String selectExistingUser(
            HttpSession session,
            @RequestParam("userId") Long userId
    ) {

        /**
         * 선택된 User PK 정보만 Session에 담아 주기
         * */
        Optional<AppUser> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            userSessionManager.setCurrentUser(session, user);
        }

        String redirectPath = "redirect:/user/select";
        return redirectPath;
    }

    /**
     * 현재 선택된 사용자 정보를 초기화합니다.
     */
    @PostMapping("/clear")
    public String clearCurrentUser(HttpSession session) {

        userSessionManager.clearCurrentUser(session);

        String redirectPath = "redirect:/user/select";
        return redirectPath;
    }
}

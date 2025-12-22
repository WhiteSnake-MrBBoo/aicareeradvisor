package com.example.ai_career_advisor.ServiceImpl.user;

import com.example.ai_career_advisor.Constant.UserGroup;
import com.example.ai_career_advisor.Entity.user.AppUser;
import com.example.ai_career_advisor.Repository.user.AppUserRepository;
import com.example.ai_career_advisor.Service.user.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 도메인 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;

    @Override
    public AppUser getOrCreateUser(String displayName, String email, UserGroup userGroup) {

        Optional<AppUser> existingUserOptional = appUserRepository.findByEmail(email);
        if (existingUserOptional.isPresent()) {
            AppUser existingUser = existingUserOptional.get();
            return existingUser;
        }

        AppUser newUser = AppUser.builder()
                .displayName(displayName)
                .email(email)
                .userGroup(userGroup)
                .build();

        AppUser savedUser = appUserRepository.save(newUser);
        return savedUser;
    }

    @Override
    public List<AppUser> getAllUsers() {
        List<AppUser> userList = appUserRepository.findAll();
        return userList;
    }

    @Override
    public Optional<AppUser> findById(Long userId) {
        Optional<AppUser> userOptional = appUserRepository.findById(userId);
        return userOptional;
    }
}

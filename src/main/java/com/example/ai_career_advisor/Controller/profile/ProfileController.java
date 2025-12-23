package com.example.ai_career_advisor.Controller.profile;

import com.example.ai_career_advisor.DTO.profile.request.ProfileCreateRequestDTO;
import com.example.ai_career_advisor.Entity.profile.core.PrfProfile;
import com.example.ai_career_advisor.Service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 프로필 관련 API 컨트롤러 (1차 테스트용)
 * - 프로필 + 매핑을 한 번에 생성하는 엔드포인트 제공
 */
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 프로필 생성 + 스킬/경험/가치관/환경/고민 매핑 저장
     * - 요청 JSON: ProfileCreateRequest
     * - 응답: 생성된 profileId
     */
    @PostMapping
    public ResponseEntity<Long> createProfile(@RequestBody ProfileCreateRequestDTO request) {

        PrfProfile createdProfile = profileService.createProfileWithDetails(request);
        Long profileId = createdProfile.getProfileId();

        ResponseEntity<Long> responseEntity =
                ResponseEntity.status(HttpStatus.CREATED).body(profileId);

        return responseEntity;
    }
}

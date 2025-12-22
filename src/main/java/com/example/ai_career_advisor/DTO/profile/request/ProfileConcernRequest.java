package com.example.ai_career_advisor.DTO.profile.request;

import lombok.*;

/**
 * 프로필 고민 항목 등록 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileConcernRequest {

    private String concernCode;
    private String detailText;
}

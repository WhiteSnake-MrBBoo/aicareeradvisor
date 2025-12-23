package com.example.ai_career_advisor.DTO.profile.request;

import com.example.ai_career_advisor.Constant.ExperienceType;
import java.time.LocalDate;
import lombok.*;

/**
 * 프로필 경험 및 포트 폴리오 등록 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileExperienceRequestDTO {

    private ExperienceType experienceType;
    private String title;
    private String organization;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String outcome;
    private String linkUrl;
}

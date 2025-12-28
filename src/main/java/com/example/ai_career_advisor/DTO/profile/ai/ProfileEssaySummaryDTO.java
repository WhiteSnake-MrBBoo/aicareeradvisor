package com.example.ai_career_advisor.DTO.profile.ai;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 프로필 상세 화면에서 자소서 히스토리 목록을 보여주기 위한 DTO
 */
@Data
@Builder
public class ProfileEssaySummaryDTO {

    private Long essayId;   //자기 소걔서 생성 아이디 DTO
    private Integer versionNo;  //프로필 별 버젼
    private boolean main;           // is_main == Y 여부
    private String title;   //생성시 저장 제목
    private String modelName;   //AI 추론 모델
    private String previewText;     // 첫 줄 또는 앞부분 50~100자 ? 저장전 preview
    private LocalDateTime regDate;  //생성 일자
}

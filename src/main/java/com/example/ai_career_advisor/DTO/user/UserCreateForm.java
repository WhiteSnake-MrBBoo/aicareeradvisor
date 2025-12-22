package com.example.ai_career_advisor.DTO.user;

import com.example.ai_career_advisor.Constant.UserGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자 신규 생성 폼 DTO
 * - View(Thymeleaf)에서 바인딩용으로 사용합니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserCreateForm {

    /** 화면 표시용 이름(닉네임/실명) */
    private String displayName;

    /** 이메일 */
    private String email;

    /** 사용자 그룹(성인/청소년) */
    private UserGroup userGroup;
}

package com.sprint.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDtoForCreate(

        @Schema(example = "구름이")
        @NotBlank(message = "유저 이름은 필수입니다.")
        @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하로 입력해주세요.")
        String username,

        @Schema(example = "비밀번호486")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @Schema(example = "codeit@codeit.com")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email) {
}



package com.sprint.mission.discodeit.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UserLoginRequestDTO", description = "사용자 로그인 요청 정보를 담은 DTO")
public record UserLoginRequestDTO(

    @Schema(description = "사용자 이메일", example = "john@example.com")
    @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

    @Schema(description = "사용자 비밀번호", example = "secret123")
    @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 char") String password
) {

}

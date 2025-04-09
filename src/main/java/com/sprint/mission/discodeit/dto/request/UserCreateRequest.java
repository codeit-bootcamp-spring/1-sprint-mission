package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "Username cannot be blank") // 사용자 이름이 비어 있지 않아야 합니다.
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") // 사용자 이름 길이 제한
    String username,

    @NotBlank(message = "Email cannot be blank") // 이메일이 비어 있지 않아야 합니다.
    @Email(message = "Invalid email format") // 유효한 이메일 형식이어야 합니다.
    String email,

    @NotBlank(message = "Password cannot be blank") // 비밀번호가 비어 있지 않아야 합니다.
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters") // 비밀번호 길이 제한
    String password
) {

}

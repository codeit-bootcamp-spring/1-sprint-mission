package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
    @NotBlank(message = "유저 이름은 필수 입력 값입니다.")
    String username,
    String email,
    @NotBlank(message = "유저 비밀번호는 필수 입력 값입니다.")
    String password
) {

}

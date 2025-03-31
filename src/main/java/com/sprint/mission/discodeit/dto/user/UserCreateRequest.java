package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "username 은 공백일 수 없습니다.")
    @Size(min = 2, max = 30, message = "username 길이는 2~30자여야 합니다.")
    String username,

    @NotBlank(message = "email 은 공백일 수 없습니다.")
    @Email(message = "email 형식이 잘못되었습니다.")
    String email,

    @NotBlank(message = "password 는 공백일 수 없습니다.")
    @Size(min = 8, message = "password 는 최소 8자 이상이어야 합니다.")
    String password
) {

}

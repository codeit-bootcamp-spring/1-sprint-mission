package com.sprint.mission.discodeit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.sprint.mission.discodeit.constant.UserConstant.PASSWORD_MIN_LENGTH;

public record CreateUserRequest(
    @NotBlank
    @Schema(example = "username")
    String username,
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, message = "password 는 최소 5글자 이상이어야 합니다.")
    String password,
    @NotBlank
    @Schema(example = "example@gmail.com")
    String email
) {

}

package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.sprint.mission.discodeit.constant.UserConstant.*;

public record CreateUserRequest(
    @NotBlank
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = "username 은 2 ~ 10글자 사이여야 합니다.")
    String username,
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, message = "password 는 최소 5글자 이상이어야 합니다.")
    String password,
    @NotBlank
    @Pattern(regexp = EMAIL_REGEX)
    String email
) {

}

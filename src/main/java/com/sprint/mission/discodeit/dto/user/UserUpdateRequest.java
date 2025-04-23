package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
    @NotBlank(message = "유저 이름은 필수 입력 값입니다.")
    String newUserName,
    String newEmail,
    String password
) {

}

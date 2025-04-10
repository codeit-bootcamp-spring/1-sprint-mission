package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthUserRequest(
    @NotBlank(message = "이름은 필수로 입력해야합니다.")
    @Size(min = 2, max = 10, message = "이름은 2자 이상, 10자 이하로 입력해야 합니다.")
    String name,
    @NotBlank(message = "비밀번호는 필수로 입력해야합니다.")
    @Size(min = 4, max = 16, message = "비밀번호는 8자 이상, 16 이하로 입력해야 합니다.")
    String password
) {

}

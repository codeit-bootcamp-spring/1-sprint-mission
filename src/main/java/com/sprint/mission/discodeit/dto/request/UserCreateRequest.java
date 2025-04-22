package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "사용자 이름은 필수입니다.")
    String username,

    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 3, max = 30, message = "비밀번호는 3자 이상 30자 이하여야 합니다.")
    String password
) {

}

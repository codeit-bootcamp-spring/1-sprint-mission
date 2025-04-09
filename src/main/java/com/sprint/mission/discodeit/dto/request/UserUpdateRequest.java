package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

    @NotBlank(message = "새 사용자 이름은 필수입니다.")
    String newUsername,

    @NotBlank(message = "새 이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String newEmail,

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Size(min = 3, max = 30, message = "비밀번호는 6자 이상 30자 이하여야 합니다.")
    String newPassword
) {

}

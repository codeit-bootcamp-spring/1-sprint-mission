package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank(message = "새 사용자 이름은 필수입니다.")
        String newUsername,

        @NotBlank(message = "새 이메일은 필수입니다.")
                @Email(message = "올바른 이메일 형식이 아닙니다.")
        String newEmail,

        @NotBlank(message = "새 비밀번호는 필수입니다.")
                @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
        String newPassword
) {
}

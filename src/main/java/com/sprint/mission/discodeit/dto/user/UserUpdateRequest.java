package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest
    (
        @Size(min = 2, max = 10, message = "이름은 2자 이상, 10자 이하로 입력해야 합니다.")
        String newName,

        @Size(min = 4, max = 16, message = "비밀번호는 8자 이상, 16 이하로 입력해야 합니다.")
        String newPassword,

        @Email(message = "이메일 형식이 맞아야합니다.")
        String newEmail
    ) {

}

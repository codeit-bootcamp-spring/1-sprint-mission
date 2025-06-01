package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

    @Size(max = 50, message = "최대 50자까지 입력할 수 있습니다. 일부 문자는 더 빨리 제한에 도달할 수 있어요.")
    String newUsername,

    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @Size(max = 100, message = "최대 100자까지 입력할 수 있습니다.")
    String newEmail,

    @Size(max = 60, message = "최대 60자까지 입력할 수 있습니다. 일부 문자는 더 빨리 제한에 도달할 수 있어요.")
    String newPassword
) {

}

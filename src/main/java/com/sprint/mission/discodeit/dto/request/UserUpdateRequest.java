package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @NotEmpty(message = "값은 비어 있을 수 없습니다.")
    @Size(min = 1, max = 50)
    String newUsername,
    @Email
    @Size(max = 100)
    String newEmail,
    @Size(max = 60)
    String newPassword
) {

}

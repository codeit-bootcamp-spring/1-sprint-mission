package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
	@NotBlank
    String newUsername,
	@NotBlank @Email
    String newEmail,
	@NotBlank
    String newPassword
) {

}

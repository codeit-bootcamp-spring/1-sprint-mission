package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
	@NotBlank
    String username,
	@NotBlank @Email
    String email,
	@NotBlank
    String password
) {

}

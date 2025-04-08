package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank
    @Size(min = 1, max = 50)
    String username,
    @NotBlank
    @Email
    @Size(max = 100)
    String email,
    @NotBlank
    @Size(max = 60)
    String password
) {

}

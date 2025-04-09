package com.sprint.mission.discodeit.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(

    @NotBlank
    String username,

    @NotBlank
    @Email
    String email,

    @NotBlank
    String password
) {

}

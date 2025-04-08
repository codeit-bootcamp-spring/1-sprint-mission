package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

    @NotNull
    @Size(min = 1, max = 50)
    String username,

    @Email
    String email,

    @NotNull
    String password
) {

}

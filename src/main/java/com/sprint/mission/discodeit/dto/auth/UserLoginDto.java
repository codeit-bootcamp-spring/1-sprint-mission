package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(

    @NotBlank
    @Size(min = 1, max = 32)
    String username,

    @NotBlank
    @Size(min = 1, max = 32)
    String password
) {

}

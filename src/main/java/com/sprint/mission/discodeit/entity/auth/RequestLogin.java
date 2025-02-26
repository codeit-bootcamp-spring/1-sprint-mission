package com.sprint.mission.discodeit.entity.auth;

import jakarta.validation.constraints.NotBlank;

public record RequestLogin(

    @NotBlank
    String username,

    @NotBlank
    String password
) {

}

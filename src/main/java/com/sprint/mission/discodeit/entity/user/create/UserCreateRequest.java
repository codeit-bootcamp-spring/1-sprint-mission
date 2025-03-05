package com.sprint.mission.discodeit.entity.user.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserCreateRequest(
    @NotBlank
    String username,

    @Email
    @NotBlank
    String email,

    @NotBlank
    @Length(min = 6, max = 20)
    String password
) {

}

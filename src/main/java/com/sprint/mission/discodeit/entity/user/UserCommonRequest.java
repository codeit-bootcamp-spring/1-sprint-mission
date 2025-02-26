package com.sprint.mission.discodeit.entity.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record UserCommonRequest(
        UUID id,

        @NotBlank
        String userName,

        @Email
        String userEmail,

        @Length(min = 6, max = 32)
        String userPassword
)
{
}

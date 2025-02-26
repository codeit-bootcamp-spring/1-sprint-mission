package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.Email;

import java.util.UUID;

public record UserRequest(
        String username,
        String password,

        @Email
        String email,

        String phoneNumber,
        UUID profileImageId
) {
}

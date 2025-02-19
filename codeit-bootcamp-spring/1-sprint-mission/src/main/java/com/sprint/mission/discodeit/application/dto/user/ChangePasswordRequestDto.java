package com.sprint.mission.discodeit.application.dto.user;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDto(
        @NotBlank
        String password
) {
}

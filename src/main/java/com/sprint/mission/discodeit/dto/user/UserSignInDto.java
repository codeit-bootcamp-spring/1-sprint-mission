package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserSignInDto(@NotBlank String username, @NotBlank String password) {
}

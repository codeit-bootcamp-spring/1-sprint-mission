package com.sprint.mission.discodeit.entity.status.user;

import jakarta.validation.constraints.NotBlank;

public record UserStatusUpdateRequest(
        @NotBlank
        String state
) {
}

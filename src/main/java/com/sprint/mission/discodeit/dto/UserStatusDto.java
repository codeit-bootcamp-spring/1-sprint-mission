package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserStatusDto(
    UUID userId,
    Long lastOnline
) {
}

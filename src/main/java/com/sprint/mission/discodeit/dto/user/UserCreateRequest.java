package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserCreateRequest(
    String password,
    String name,
    String email,
    UUID profileImageId
) {
}

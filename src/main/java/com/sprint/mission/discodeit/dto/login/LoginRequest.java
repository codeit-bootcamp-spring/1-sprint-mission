package com.sprint.mission.discodeit.dto.login;

import java.util.UUID;

public record LoginRequest(
    UUID userId,
    String password
) {

}

package com.sprint.mission.discodeit.dto.user;

public record UserLoginRequest(
        String username,
        String password
) {
}

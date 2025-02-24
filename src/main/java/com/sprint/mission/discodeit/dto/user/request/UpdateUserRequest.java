package com.sprint.mission.discodeit.dto.user.request;

public record UpdateUserRequest(
        String name,
        String nickname,
        String email,
        String password
) {
}

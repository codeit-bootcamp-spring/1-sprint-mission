package com.sprint.mission.discodeit.dto.user.request;

public record CreateUserRequest(
        String name,
        String nickname,
        String email,
        String password
) {
}

package com.sprint.mission.discodeit.dto.request.users.create;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {
}

package com.sprint.mission.discodeit.dto;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {
}

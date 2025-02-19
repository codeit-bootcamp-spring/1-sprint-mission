package com.sprint.mission.discodeit.dto.userService;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {}

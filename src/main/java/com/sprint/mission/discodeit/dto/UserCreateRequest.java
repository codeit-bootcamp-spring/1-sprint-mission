package com.sprint.mission.discodeit.dto;

public record UserCreateRequest(
        String userName,
        String email,
        String password
) {}

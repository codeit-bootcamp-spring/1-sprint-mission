package com.sprint.mission.discodeit.dto.request;

public record UserRequest(
        String email,
        String password,
        String name
) {}

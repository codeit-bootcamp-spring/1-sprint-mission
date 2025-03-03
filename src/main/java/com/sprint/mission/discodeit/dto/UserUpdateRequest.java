package com.sprint.mission.discodeit.dto;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String password
) {}

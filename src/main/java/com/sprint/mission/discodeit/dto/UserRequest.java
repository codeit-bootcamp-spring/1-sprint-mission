package com.sprint.mission.discodeit.dto;

public record UserRequest(
        String username,
        String password,
        String email,
        String phoneNumber,
        Long profileImageId
) {
}

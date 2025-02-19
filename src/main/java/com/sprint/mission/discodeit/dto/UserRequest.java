package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserRequest(
        String username,
        String password,
        String email,
        String phoneNumber,
        UUID profileImageId
//        String profileImageName
) {
}

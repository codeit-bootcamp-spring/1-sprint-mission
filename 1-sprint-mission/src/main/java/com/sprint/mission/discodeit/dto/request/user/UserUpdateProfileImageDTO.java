package com.sprint.mission.discodeit.dto.request.user;

import java.util.UUID;
public record UserUpdateProfileImageDTO(
        UUID userId, String fileName ,
        String contentType,
        byte[] imageData
) {
}

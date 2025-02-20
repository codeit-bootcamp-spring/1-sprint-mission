package com.sprint.mission.discodeit.dto.request.user;

import java.util.UUID;

public record UserProfileImageDTO(UUID userId, String fileName ,String contentType, byte[] imageData) {
}

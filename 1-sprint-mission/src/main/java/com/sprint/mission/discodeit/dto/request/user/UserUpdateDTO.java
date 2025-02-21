package com.sprint.mission.discodeit.dto.request.user;

import java.util.UUID;

public record UserUpdateDTO(UUID id, String newUsername, String newEmail, String newPassword) {
}

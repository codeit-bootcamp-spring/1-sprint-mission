package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserUpdateDTO(UUID id, String newUsername, String newEmail, String newPassword) {
}

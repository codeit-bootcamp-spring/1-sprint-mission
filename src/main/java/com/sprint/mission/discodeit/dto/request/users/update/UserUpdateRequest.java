package com.sprint.mission.discodeit.dto.request.users.update;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPassword
) {
}

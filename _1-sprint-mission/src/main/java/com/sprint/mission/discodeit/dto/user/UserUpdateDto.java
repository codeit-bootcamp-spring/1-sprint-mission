package com.sprint.mission.discodeit.dto.user;

public record UserUpdateDto(
        String newUsername,
        String newEmail,
        String newPassword,
        String newPhonenumber,
        String newAddress
) {
}

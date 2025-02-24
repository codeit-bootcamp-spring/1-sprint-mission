package com.sprint.mission.discodeit.dto.userDto;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}

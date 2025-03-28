package com.sprint.mission.discodeit.dto.user;

public record UpdateUserRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}

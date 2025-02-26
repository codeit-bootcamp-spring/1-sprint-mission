package com.sprint.mission.discodeit.dto.user.request;

public record UpdateUserRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}

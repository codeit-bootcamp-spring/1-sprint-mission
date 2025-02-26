package com.sprint.mission.discodeit.dto.request;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}

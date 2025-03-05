package com.sprint.mission.discodeit.entity.user.update;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}

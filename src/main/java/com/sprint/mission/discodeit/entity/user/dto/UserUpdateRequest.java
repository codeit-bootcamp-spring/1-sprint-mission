package com.sprint.mission.discodeit.entity.user.dto;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}

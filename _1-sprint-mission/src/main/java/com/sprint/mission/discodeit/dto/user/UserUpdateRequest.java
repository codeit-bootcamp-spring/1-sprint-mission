package com.sprint.mission.discodeit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수정할 User 정보")
public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword,
    String newPhonenumber,
    String newAddress
) {

}

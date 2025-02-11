package com.sprint.mission.discodeit.application.dto.user;

import com.sprint.mission.discodeit.domain.userstatus.enums.OnlineStatus;

public record UserResponseDto(
    String nickname,
    String username,
    String email,
    OnlineStatus onlineStatus
) {

}

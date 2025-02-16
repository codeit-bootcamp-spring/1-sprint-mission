package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

public record UserDto(
        User user,
        byte[] profileImageUrl, //Optional..?
        UserStatus userStatus
) {
}

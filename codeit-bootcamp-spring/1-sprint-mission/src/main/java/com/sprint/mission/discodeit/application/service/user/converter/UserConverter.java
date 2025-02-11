package com.sprint.mission.discodeit.application.service.user.converter;

import com.sprint.mission.discodeit.application.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserResponseDto toDto(User user, UserStatus userStatus) {
        return new UserResponseDto(user.getNicknameValue(), user.getUsernameValue(), user.getEmailValue(),
                userStatus.getOnlineStatus());
    }
}

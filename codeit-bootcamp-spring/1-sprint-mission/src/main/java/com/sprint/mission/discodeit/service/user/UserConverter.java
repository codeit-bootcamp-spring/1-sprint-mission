package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserName;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;

public class UserConverter {

    public User toEntity(RegisterUserRequest request) {
        var user = new User(
                new UserName(request.name())
        );
        return user;
    }

    public UserInfoResponse toDto(User user) {
        var dto = new UserInfoResponse(
                user.getId(),
                user.getName()
        );
        return dto;
    }
}

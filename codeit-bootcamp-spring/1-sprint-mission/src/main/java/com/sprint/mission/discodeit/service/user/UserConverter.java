package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserName;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;

public class UserConverter {

    public User toEntity(RegisterUserRequest request) {
        try {
            var user = new User(
                    new UserName(request.name())
            );
            return user;
        } catch (IllegalArgumentException e) {
            // TODO : 예외 처리 더 딥하게 학습해보기
            throw new UserException(e.getMessage(), e);
        }
    }

    public UserInfoResponse toDto(User user) {
        var dto = new UserInfoResponse(
                user.getId(),
                user.getName()
        );
        return dto;
    }
}

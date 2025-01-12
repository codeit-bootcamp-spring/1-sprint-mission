package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserName;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;
import java.util.Objects;

public class UserConverter {
    private static UserConverter INSTANCE;

    public UserConverter() {}

    public static UserConverter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserConverter();
        }
        return Objects.requireNonNull(INSTANCE);
    }

    public User toEntity(RegisterUserRequest request) {
        try {
            var user = User.from(request.name());
            return user;
        } catch (IllegalArgumentException e) {
            // TODO validator 사용으로 예외가 안던져지는 문제 발생
            throw UserException.of(e.getMessage(), e);
        }
    }

    public UserInfoResponse toDto(User user) {
        var dto = new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getStatus()
        );
        return dto;
    }
}

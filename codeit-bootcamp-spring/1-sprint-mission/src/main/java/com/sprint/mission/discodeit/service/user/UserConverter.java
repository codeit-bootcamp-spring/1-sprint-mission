package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;
import com.sprint.mission.discodeit.entity.user.entity.User;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private static UserConverter INSTANCE;

    public UserConverter() {
    }

    public static UserConverter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserConverter();
        }
        return Objects.requireNonNull(INSTANCE);
    }

    public User toEntity(RegisterUserRequest request) {
        var createdUser = User.createFrom(request.name());
        return createdUser;
    }

    public UserInfoResponse toDto(User user) {
        var responseDto = new UserInfoResponse(
                user.getId(),
                user.getUserName(),
                user.getStatus()
        );
        return responseDto;
    }
}

package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.response.FindUserResponse;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.dto.user.response.UpdateUserResponse;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(CreateUserRequest createUserRequest) {
        return User.of(createUserRequest.name(), createUserRequest.nickname(), createUserRequest.email(),
                createUserRequest.password());
    }

    public CreateUserResponse toCreateUserResponse(User user) {
        return new CreateUserResponse(user.getId(), user.getName(), user.getNickname(), user.getEmail(), user.getPassword());
    }

    public FindUserResponse toFindUserResponse(User user, byte[] profileImage, boolean isOnline) {
        return new FindUserResponse(user.getName(), user.getNickname(), user.getEmail(), profileImage, isOnline);
    }

    public UpdateUserResponse toUpdateUserResponse(User user) {
        return new UpdateUserResponse(user.getId(), user.getName(), user.getNickname(), user.getEmail(), user.getPassword());
    }

    public LoginResponse toLoginResponse(User user) {
        return new LoginResponse(user.getId(), user.getNickname(), user.getEmail());
    }
}

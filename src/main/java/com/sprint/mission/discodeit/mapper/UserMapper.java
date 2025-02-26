package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.response.FindUserResponse;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User toEntity(CreateUserRequest createUserRequest) {
    return User.of(createUserRequest.username(), "nickname", createUserRequest.email(),
        createUserRequest.password());
  }

  public UserDto toUserDto(User user, UUID profileId) {
    return new UserDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(),
        user.getEmail(), user.getPassword(), profileId);
  }

  public FindUserResponse toFindUserResponse(User user, UUID profileId, boolean isOnline) {
    return new FindUserResponse(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
        user.getName(), user.getEmail(), profileId, isOnline);
  }

  public LoginResponse toLoginResponse(User user) {
    return new LoginResponse(user.getId(), user.getNickname(), user.getEmail());
  }
}

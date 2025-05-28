package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BinaryContentMapper binaryContentMapper;

    public UserResponse entityToDto(User user) {
        return entityToDto(user, null);
    }

    public UserResponse entityToDto(User user, Boolean isOnline) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .profile(binaryContentMapper.entityToDto(user.getProfile()))
            .online(isOnline)
            .role(user.getRole().name())
            .build();
    }
}


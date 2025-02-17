package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userEntityToDto(User user, boolean isOnline){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getProfileId(),
                isOnline,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

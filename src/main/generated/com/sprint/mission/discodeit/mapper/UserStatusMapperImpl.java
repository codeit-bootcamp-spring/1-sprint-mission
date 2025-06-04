package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2025-03-28T16:45:12+0900",
        comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class UserStatusMapperImpl implements UserStatusMapper {

    @Override
    public UserStatusDto toDto(UserStatus entity) {
        if (entity == null) {
            return null;
        }

        UserStatusDto userStatusDto = new UserStatusDto();

        userStatusDto.setUserId(entityUserId(entity));
        userStatusDto.setId(entity.getId());
        userStatusDto.setLastActiveAt(entity.getLastActiveAt());

        return userStatusDto;
    }

    private UUID entityUserId(UserStatus userStatus) {
        User user = userStatus.getUser();
        if (user == null) {
            return null;
        }
        return user.getId();
    }
}

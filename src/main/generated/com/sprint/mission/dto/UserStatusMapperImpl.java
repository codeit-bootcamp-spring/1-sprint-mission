package com.sprint.mission.dto;

import com.sprint.mission.dto.response.UserStatusDto;
import com.sprint.mission.entity.addOn.UserStatus;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class UserStatusMapperImpl implements UserStatusMapper {

    @Override
    public UserStatusDto toDto(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID id = null;

        id = userStatus.getId();

        UUID userId = null;
        Instant lastActivityAt = null;

        UserStatusDto userStatusDto = new UserStatusDto( id, userId, lastActivityAt );

        return userStatusDto;
    }
}

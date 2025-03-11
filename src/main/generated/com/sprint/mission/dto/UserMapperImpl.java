package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.BinaryContentDto;
import com.sprint.mission.dto.mappedDto.UserDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        Boolean online = null;
        UUID id = null;
        String username = null;
        String email = null;
        BinaryContentDto profile = null;

        online = userStatusOnline( user );
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        profile = binaryContentToBinaryContentDto( user.getProfile() );

        UserDto userDto = new UserDto( id, username, email, profile, online );

        return userDto;
    }

    private Boolean userStatusOnline(User user) {
        UserStatus status = user.getStatus();
        if ( status == null ) {
            return null;
        }
        return status.isOnline();
    }

    protected BinaryContentDto binaryContentToBinaryContentDto(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        UUID id = null;
        String fileName = null;
        Long size = null;
        String contentType = null;

        id = binaryContent.getId();
        fileName = binaryContent.getFileName();
        size = binaryContent.getSize();
        contentType = binaryContent.getContentType();

        byte[] bytes = null;

        BinaryContentDto binaryContentDto = new BinaryContentDto( id, fileName, size, contentType, bytes );

        return binaryContentDto;
    }
}

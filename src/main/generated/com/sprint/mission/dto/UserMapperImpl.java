package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.BinaryContentDto;
import com.sprint.mission.dto.mappedDto.UserDto;
import com.sprint.mission.dto.request.UserDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
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

        UUID id = null;
        String username = null;
        String email = null;
        BinaryContentDto profile = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        profile = binaryContentToBinaryContentDto( user.getProfile() );

        Boolean online = user.getStatus() != null ? user.getStatus().isOnline() : null;

        UserDto userDto = new UserDto( id, username, email, profile, online );

        return userDto;
    }

    @Override
    public User toEntityWithoutProfile(UserDtoForCreate userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( userDto.username() );
        user.email( userDto.email() );
        user.password( userDto.password() );

        return user.build();
    }

    @Override
    public User toEntityWithProfile(UserDtoForCreate userDto, BinaryContent profile) {
        if ( userDto == null && profile == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        if ( userDto != null ) {
            user.username( userDto.username() );
            user.email( userDto.email() );
            user.password( userDto.password() );
        }
        user.profile( profile );

        return user.build();
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

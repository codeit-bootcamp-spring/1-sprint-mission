package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.BinaryContentDto;
import com.sprint.mission.dto.mappedDto.UserDto;
import com.sprint.mission.dto.response.SaveUserDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.User;
import java.time.Instant;
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

        Boolean online = null;

        UserDto userDto = new UserDto( id, username, email, profile, online );

        return userDto;
    }

    @Override
    public SaveUserDto toDtoForSave(User user) {
        if ( user == null ) {
            return null;
        }

        String name = null;
        UUID profileImgId = null;
        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String email = null;

        name = user.getUsername();
        profileImgId = userProfileId( user );
        id = user.getId();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
        email = user.getEmail();

        SaveUserDto saveUserDto = new SaveUserDto( id, createdAt, updatedAt, name, email, profileImgId );

        return saveUserDto;
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

    private UUID userProfileId(User user) {
        BinaryContent profile = user.getProfile();
        if ( profile == null ) {
            return null;
        }
        return profile.getId();
    }
}

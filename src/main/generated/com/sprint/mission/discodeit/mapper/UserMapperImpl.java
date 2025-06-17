package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T11:17:26+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private BinaryContentMapper binaryContentMapper;

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        String email = null;
        BinaryContentDto profile = null;
        Role role = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        profile = binaryContentMapper.toDto( user.getProfile() );
        role = user.getRole();

        Boolean online = null;

        UserDto userDto = new UserDto( id, username, email, profile, online, role );

        return userDto;
    }

    @Override
    public UserDto toDto(User user, boolean online) {
        if ( user == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        String email = null;
        BinaryContentDto profile = null;
        Role role = null;
        if ( user != null ) {
            id = user.getId();
            username = user.getUsername();
            email = user.getEmail();
            profile = binaryContentMapper.toDto( user.getProfile() );
            role = user.getRole();
        }

        Boolean online1 = online;

        UserDto userDto = new UserDto( id, username, email, profile, online1, role );

        return userDto;
    }
}

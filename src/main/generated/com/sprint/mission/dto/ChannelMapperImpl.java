package com.sprint.mission.dto;

import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.BinaryContentDto;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.dto.response.UserDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ChannelMapperImpl implements ChannelMapper {

    @Override
    public ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt) {
        if ( channel == null && participants == null && lastMessageAt == null ) {
            return null;
        }

        UUID id = null;
        String description = null;
        String name = null;
        ChannelType channelType = null;
        if ( channel != null ) {
            id = channel.getId();
            description = channel.getDescription();
            name = channel.getName();
            channelType = channel.getChannelType();
        }
        List<UserDto> participants1 = null;
        participants1 = userListToUserDtoList( participants );
        Instant lastMessageAt1 = null;
        lastMessageAt1 = lastMessageAt;

        ChannelDto channelDto = new ChannelDto( id, channelType, name, description, participants1, lastMessageAt1 );

        return channelDto;
    }

    @Override
    public UserDto UserToUserDto(User user) {
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
    public ChannelDto toDto(Channel channel) {
        if ( channel == null ) {
            return null;
        }

        UUID id = null;
        ChannelType channelType = null;
        String name = null;
        String description = null;

        id = channel.getId();
        channelType = channel.getChannelType();
        name = channel.getName();
        description = channel.getDescription();

        List<UserDto> participants = null;
        Instant lastMessageAt = null;

        ChannelDto channelDto = new ChannelDto( id, channelType, name, description, participants, lastMessageAt );

        return channelDto;
    }

    @Override
    public Channel toPublicEntity(PublicChannelCreateDTO request, ChannelType channelType) {
        if ( request == null && channelType == null ) {
            return null;
        }

        String name = null;
        String description = null;
        if ( request != null ) {
            name = request.name();
            description = request.description();
        }
        ChannelType channelType1 = null;
        channelType1 = channelType;

        Channel channel = new Channel( name, description, channelType1 );

        return channel;
    }

    @Override
    public Channel toPrivateEntity(ChannelType channelType) {
        if ( channelType == null ) {
            return null;
        }

        ChannelType channelType1 = null;

        channelType1 = channelType;

        String name = null;
        String description = null;

        Channel channel = new Channel( name, description, channelType1 );

        return channel;
    }

    protected List<UserDto> userListToUserDtoList(List<User> list) {
        if ( list == null ) {
            return null;
        }

        List<UserDto> list1 = new ArrayList<UserDto>( list.size() );
        for ( User user : list ) {
            list1.add( UserToUserDto( user ) );
        }

        return list1;
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

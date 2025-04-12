package com.sprint.mission.dto;

import com.sprint.mission.dto.response.BinaryContentDto;
import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.UserDto;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageDto toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        UUID channelId = null;
        UserDto author = null;
        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String content = null;

        channelId = messageChannelId( message );
        author = userToUserDto( message.getAuthor() );
        id = message.getId();
        createdAt = message.getCreatedAt();
        updatedAt = message.getUpdatedAt();
        content = message.getContent();

        List<BinaryContentDto> attachments = null;

        MessageDto messageDto = new MessageDto( id, createdAt, updatedAt, content, channelId, author, attachments );

        return messageDto;
    }

    @Override
    public UserDto userToUserDto(User user) {
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
    public Message toEntity(Channel channel, User author, String content) {
        if ( channel == null && author == null && content == null ) {
            return null;
        }

        Channel channel1 = null;
        channel1 = channel;
        User author1 = null;
        author1 = author;
        String content1 = null;
        content1 = content;

        Message message = new Message( content1, channel1, author1 );

        return message;
    }

    private UUID messageChannelId(Message message) {
        Channel channel = message.getChannel();
        if ( channel == null ) {
            return null;
        }
        return channel.getId();
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

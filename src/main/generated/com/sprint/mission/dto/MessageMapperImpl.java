package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.BinaryContentDto;
import com.sprint.mission.dto.mappedDto.MessageDto;
import com.sprint.mission.dto.mappedDto.UserDto;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
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
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageDto toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        UUID channelId = null;
        UUID id = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        String content = null;
        UserDto author = null;
        List<BinaryContentDto> attachments = null;

        channelId = messageChannelId( message );
        id = message.getId();
        createdAt = message.getCreatedAt();
        updatedAt = message.getUpdatedAt();
        content = message.getContent();
        author = userToUserDto( message.getAuthor() );
        attachments = binaryContentListToBinaryContentDtoList( message.getAttachments() );

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
    public Message toEntity(MessageDtoForCreate responseDto, Channel channel, User author) {
        if ( responseDto == null && channel == null && author == null ) {
            return null;
        }

        Message message = new Message();

        if ( responseDto != null ) {
            message.setContent( responseDto.content() );
        }
        message.setChannel( channel );
        message.setAuthor( author );

        return message;
    }

    @Override
    public Message update(MessageDtoForUpdate updateDto, Message updatingMessage) {
        if ( updateDto == null ) {
            return updatingMessage;
        }

        updatingMessage.setContent( updateDto.content() );

        return updatingMessage;
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

    protected List<BinaryContentDto> binaryContentListToBinaryContentDtoList(List<BinaryContent> list) {
        if ( list == null ) {
            return null;
        }

        List<BinaryContentDto> list1 = new ArrayList<BinaryContentDto>( list.size() );
        for ( BinaryContent binaryContent : list ) {
            list1.add( binaryContentToBinaryContentDto( binaryContent ) );
        }

        return list1;
    }
}

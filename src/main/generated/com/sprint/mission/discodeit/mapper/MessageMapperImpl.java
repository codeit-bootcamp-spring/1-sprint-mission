package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-13T09:40:47+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageDto toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageDto messageDto = new MessageDto();

        messageDto.setId( message.getId() );
        messageDto.setCreatedAt( message.getCreatedAt() );
        messageDto.setUpdatedAt( message.getUpdatedAt() );
        messageDto.setContent( message.getContent() );
        messageDto.setAuthor( userToUserDto( message.getAuthor() ) );
        messageDto.setAttachments( messageAttachmentSetToBinaryContentDtoList( message.getAttachments() ) );

        return messageDto;
    }

    protected BinaryContentDto binaryContentToBinaryContentDto(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        BinaryContentDto binaryContentDto = new BinaryContentDto();

        binaryContentDto.setId( binaryContent.getId() );
        binaryContentDto.setFileName( binaryContent.getFileName() );
        binaryContentDto.setSize( binaryContent.getSize() );
        binaryContentDto.setContentType( binaryContent.getContentType() );

        return binaryContentDto;
    }

    protected UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setUsername( user.getUsername() );
        userDto.setEmail( user.getEmail() );
        userDto.setProfile( binaryContentToBinaryContentDto( user.getProfile() ) );

        return userDto;
    }

    protected BinaryContentDto messageAttachmentToBinaryContentDto(MessageAttachment messageAttachment) {
        if ( messageAttachment == null ) {
            return null;
        }

        BinaryContentDto binaryContentDto = new BinaryContentDto();

        return binaryContentDto;
    }

    protected List<BinaryContentDto> messageAttachmentSetToBinaryContentDtoList(Set<MessageAttachment> set) {
        if ( set == null ) {
            return null;
        }

        List<BinaryContentDto> list = new ArrayList<BinaryContentDto>( set.size() );
        for ( MessageAttachment messageAttachment : set ) {
            list.add( messageAttachmentToBinaryContentDto( messageAttachment ) );
        }

        return list;
    }
}

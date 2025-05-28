package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

    //    @Mapping(target = "author", expression = "java(userMapper.toUserDto(message.getSender(), online))")
    @Mapping(target = "author", expression = "java(userMapper.toUserDto(message.getSender(), online))")
    @Mapping(target = "channelId", expression = "java(message.getChannel().getId())")
    @Mapping(target = "attachments", source = "message.attachmentsList")
    MessageDto toMessageDto(Message message, boolean online, @Context UserMapper userMapper);
}

package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class, imports = BinaryContentUtil.class)
public interface MessageMapper {

  @Mapping(target = "isEdited", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "binaryContents", ignore = true)
  Message toEntity(CreateMessageDto dto);

  @Mapping(target = "messageId", source = "message.UUID")
  @Mapping(target = "userId", source = "message.userId")
  @Mapping(target = "channelId", source = "message.channelId")
  @Mapping(target = "content", source = "message.content")
  @Mapping(target = "createdAt", source = "message.createdAt")
  @Mapping(target = "base64Data", expression = "java(BinaryContentUtil.convertMultipleBinaryContentToBase64(message.getBinaryContents()))")
  MessageResponseDto toResponseDto(Message message);

}

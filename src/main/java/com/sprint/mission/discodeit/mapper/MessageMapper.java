package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import org.mapstruct.*;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class, imports = BinaryContentUtil.class)
public interface MessageMapper {

  @Mapping(target = "UUID", ignore = true)
  @Mapping(target = "isEdited", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "channelId", source = "channelId")
  @Mapping(target = "binaryContents", ignore = true)
  Message toEntity(CreateMessageDto dto, String channelId);

  @Mapping(target = "messageId", source = "UUID")
  @Mapping(target = "userId", source = "userId")
  @Mapping(target = "channelId", source = "channelId")
  @Mapping(target = "content", source = "content")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "base64Data", source = "binaryContents", qualifiedByName = "convertToBase64")
  MessageResponseDto toResponseDto(Message message);

  @AfterMapping
  default void mapBinaryContents(@MappingTarget Message message, CreateMessageDto dto, String channelId, BinaryContentMapper binaryContentMapper){
    if(dto.getMultipart() != null && dto.getMultipart().isEmpty()){
      List<BinaryContent> binaryContents = binaryContentMapper.fromMessageFiles(
          dto.getMultipart(),
          dto.getUserId(),
          channelId,
          message.getUUID()
      );
      message.setBinaryContents(binaryContents);
    }else{
      message.setBinaryContents(Collections.emptyList());
    }
  }

  @Named("convertToBase64")
  default List<String> convertToBase64(List<BinaryContent> binaryContents){
    return binaryContents == null || binaryContents.isEmpty()
        ? Collections.emptyList()
        : binaryContents.stream()
        .map(content -> Base64.getEncoder().encodeToString(content.getData()))
        .toList();
  }
}

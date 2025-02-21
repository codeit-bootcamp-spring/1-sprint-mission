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

@Mapper(
    componentModel = "spring",
    uses = BinaryContentMapper.class,
    imports = BinaryContentUtil.class,
    builder = @Builder(disableBuilder = false)
)
public interface MessageMapper {

  @Mapping(target = "UUID", ignore = true)
  @Mapping(target = "isEdited", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "channelId", source = "channelId")
  @Mapping(target = "binaryContents", ignore = true)
  Message toEntity(CreateMessageDto dto, String channelId, @Context BinaryContentMapper binaryContentMapper);

  @Mapping(target = "messageId", source = "UUID")
  @Mapping(target = "userId", source = "userId")
  @Mapping(target = "channelId", source = "channelId")
  @Mapping(target = "content", source = "content")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "base64Data", source = "binaryContents", qualifiedByName = "convertToBase64")
  MessageResponseDto toResponseDto(Message message);

  @AfterMapping
  default void mapBinaryContents(
      @MappingTarget Message.MessageBuilder messageBuilder,
      CreateMessageDto dto,
      String channelId,
      @Context BinaryContentMapper binaryContentMapper) {

    List<BinaryContent> binaryContents = (dto.getMultipart() != null && !dto.getMultipart().isEmpty())
        ? binaryContentMapper.fromMessageFiles(dto.getMultipart(), dto.getUserId(), channelId, messageBuilder.getUUID())
        : null;

    messageBuilder.binaryContents(binaryContents);
  }

  @Named("convertToBase64")
  default List<String> convertToBase64(List<BinaryContent> binaryContents) {
    return binaryContents == null || binaryContents.isEmpty()
        ? null
        : binaryContents.stream()
        .map(content -> Base64.getEncoder().encodeToString(content.getData()))
        .toList();
  }
}

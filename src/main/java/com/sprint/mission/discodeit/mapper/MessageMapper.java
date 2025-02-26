package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import org.mapstruct.*;

import java.util.Base64;
import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = BinaryContentMapper.class,
    imports = BinaryContentUtil.class,
    builder = @Builder(disableBuilder = false)
)
public interface MessageMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "isEdited", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Message toEntity(CreateMessageDto dto, String channelId, @Context BinaryContentMapper binaryContentMapper);

  @Mapping(target = "id", source = "id")
  MessageResponseDto toResponseDto(Message message);


  @Named("convertToBase64")
  default List<String> convertToBase64(List<BinaryContent> binaryContents) {
    return binaryContents == null || binaryContents.isEmpty()
        ? null
        : binaryContents.stream()
        .map(content -> Base64.getEncoder().encodeToString(content.getData()))
        .toList();
  }
}

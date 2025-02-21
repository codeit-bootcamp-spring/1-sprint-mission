package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;

public record MessageResponseDto(
    String messageId,
    String userId,
    String channelId,
    String content,
    Instant createdAt,
    List<BinaryContentDto> data
) {
  public static MessageResponseDto fromBinaryContentDto(Message message, List<BinaryContentDto> contents) {
    return new MessageResponseDto(message.getUUID(), message.getUserUUID(), message.getChannelUUID(), message.getContent(), message.getCreatedAt(), contents);
  }

  public static MessageResponseDto fromBinaryContent(Message message, List<BinaryContent> contents){

    List<BinaryContentDto> returnContents = contents.stream().map(content -> {
      return new BinaryContentDto(content.getFileName(), content.getFileType(), content.getFileSize(), content.getData());
    }).toList();

    return new MessageResponseDto(message.getUUID(), message.getUserUUID(), message.getChannelUUID(), message.getContent(), message.getCreatedAt(), returnContents);
  }
}

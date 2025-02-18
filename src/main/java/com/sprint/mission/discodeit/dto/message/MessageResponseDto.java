package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.Base64;
import java.util.List;

public record MessageResponseDto(
    String messageId,
    String userId,
    String channelId,
    String content,
    Instant createdAt,
    List<String> base64Data
) {

  public static MessageResponseDto from(Message message){

    List<String> base64Encoded = message.getBinaryContents().stream().map(content -> {
      return Base64.getEncoder().encodeToString(content.getData());
    }).toList();

    return new MessageResponseDto(
        message.getUUID(),
        message.getUserId(),
        message.getChannelId(),
        message.getContent(),
        message.getCreatedAt(),
        base64Encoded
    );
  }
}

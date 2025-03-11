package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record MessageResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserResponse author,
    List<BinaryContentResponse> attachments
) {

//  public static MessageResponse entityToDto(Message message) {
//    return MessageResponse.builder()
//        .id(message.getId())
//        .author(message.getAuthor())
//        .channelId(message.getChannel().getId())
//        .content(message.getContent())
//        .createdAt(message.getCreatedAt())
//        .updatedAt(message.getUpdatedAt())
//        .build();
//  }
}

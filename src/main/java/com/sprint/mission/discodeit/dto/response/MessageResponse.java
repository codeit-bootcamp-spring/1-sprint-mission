package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/*
  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  //
  private String content;
  //
  private UUID channelId;
  private UUID authorId;
  private List<UUID> attachmentIds;
 */

public record MessageResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds
) {

  public static MessageResponse from(Message message) {
    return new MessageResponse(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannelId(),
        message.getAuthorId()
        , message.getAttachmentIds());
  }
}

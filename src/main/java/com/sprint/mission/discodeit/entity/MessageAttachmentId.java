package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
public class MessageAttachmentId implements Serializable {

  private UUID message;
  private UUID attachmentId;

  public MessageAttachmentId(UUID message, UUID attachmentId) {
    this.message = message;
    this.attachmentId = attachmentId;
  }
}
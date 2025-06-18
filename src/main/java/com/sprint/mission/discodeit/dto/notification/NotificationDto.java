package com.sprint.mission.discodeit.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.sprint.mission.discodeit.common.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
public class NotificationDto {

  private final UUID id;
  
  @JsonSerialize(using = InstantSerializer.class)
  @JsonDeserialize(using = InstantDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "UTC")
  private final Instant createdAt;
  private final UUID receiverId;
  private final String title;
  private final String content;
  private final NotificationType type;
  private final UUID targetId;

  @Builder
  private NotificationDto(UUID id, Instant createdAt, UUID receiverId, String title, String content,
      NotificationType type, UUID targetId) {
    this.id = id;
    this.createdAt = createdAt;
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }

}

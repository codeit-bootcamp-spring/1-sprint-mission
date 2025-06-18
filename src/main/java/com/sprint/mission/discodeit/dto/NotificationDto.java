package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationDto {

  UUID id;
  Instant createdAt;
  UUID receiverId;
  String title;
  String content;
  NotificationType type;
  UUID targetId;
}

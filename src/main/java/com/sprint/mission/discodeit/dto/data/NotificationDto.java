package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
  private UUID id;
  private Instant createdAt;
  private UUID receiverId;
  private String title;
  private String content;
  private NotificationType type;
  private UUID targetId;

}

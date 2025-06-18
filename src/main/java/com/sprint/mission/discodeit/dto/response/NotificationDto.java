package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.notification.NotificationType;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

  UUID id;
  Instant createdAt;
  UUID receiverId;
  String title;
  String content;
  NotificationType type;
  Optional<UUID> targetId;

}

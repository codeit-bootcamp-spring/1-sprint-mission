package com.sprint.mission.discodeit.async.event;

import com.sprint.mission.discodeit.common.NotificationType;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleChangedNotificationEvent {

  private UUID receiverId;
  private UUID userId;
  private NotificationType type;

}

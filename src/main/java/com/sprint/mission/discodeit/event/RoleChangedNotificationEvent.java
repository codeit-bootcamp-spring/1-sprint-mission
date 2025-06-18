package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class RoleChangedNotificationEvent extends NotificationEvent {

  public RoleChangedNotificationEvent(UUID receiver, UUID userId, String newRole) {
    super(receiver, NotificationType.ROLE_CHANGED, userId,
        "역할 변경", "당신의 권한이 " + newRole + "로 변경되었습니다.");
  }

}

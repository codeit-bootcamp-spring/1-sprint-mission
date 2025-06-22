package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserRoleChangedEvent extends NotificationEvent {

  public UserRoleChangedEvent(UUID receiver, UUID userId, String previousRole, String newRole) {
    super(receiver, NotificationType.ROLE_CHANGED, userId,
        "역할 변경", "당신의 권한이 [" + previousRole + "]에서 [" + newRole + "]로 변경되었습니다.");
  }

}

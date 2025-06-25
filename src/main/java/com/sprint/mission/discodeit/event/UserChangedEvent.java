package com.sprint.mission.discodeit.event;

import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserChangedEvent {

  private final UUID userId;
  private final Set<UUID> affectedUserIds; // 알림을 받아야 하는 사용자 목록
  private final ChangeType changeType;

  public enum ChangeType {
    PROFILE_UPDATED, STATUS_CHANGED, DELETED
  }
}

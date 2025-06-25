package com.sprint.mission.discodeit.event;

import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChannelChangedEvent {

  private final UUID channelId;
  private final Set<UUID> affectedUserIds; // 영향받는 사용자 목록
  private final ChangeType changeType;

  public enum ChangeType {
    CREATED, UPDATED, DELETED, MEMBER_ADDED, MEMBER_REMOVED
  }
}

package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ReadStatus extends BaseEntity {

  private final User user;
  private final Channel channel;
  private Instant lastReadAt;
  // 메세지를 읽을 때 updatedAt이 변경됨으로 updatedAt을 통해 메세지를 마지막으로 읽은 시간을 알아낼 수 있다.
  // 변경 가능한 필드가 updatedAt 하나이기에 가능하다.

  private ReadStatus(User user, Channel channel, Instant lastReadAt) {
    super();
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public static ReadStatus of(User user, Channel channel, Instant lastReadAt) {
    if (user == null) {
      throw new IllegalArgumentException(ErrorCode.USER_NOT_NULL.getMessage());
    }
    if (channel == null) {
      throw new IllegalArgumentException(ErrorCode.CHANNEL_NOT_NULL.getMessage());
    }
    return new ReadStatus(user, channel, lastReadAt);
  }

  public void updateLastReadAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
    updateUpdatedAt();
  }
}

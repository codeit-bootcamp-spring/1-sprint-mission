package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private UUID userId;

  private boolean online;
  private Instant now;
  private Instant lastActiveAt;

  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.online = false;
    this.now = Instant.now();

    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  // 디스코드는 접속을 무엇으로 판단하느냐
  // 1. 로그인
  // 2. 채팅 보내기, 음성 채널 참가
  // 3. UI 조작
  // + 사용자가 설정 적용할 수 있다
  public void refreshLastConnectAt(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
  }

  public boolean isConnectedNow() {
    this.now = Instant.now();
    online = Duration.between(this.lastActiveAt, this.now).toMinutes() <= 5;
    return online;
  }
}

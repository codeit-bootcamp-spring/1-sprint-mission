package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "user_statuses")
@Getter
@AllArgsConstructor
@Builder
public class UserStatus extends BaseUpdatableEntity {

  @Column(nullable = false)
  private Instant lastActiveAt;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  // 다이어그램에는 없는데 Mapper 용으로 생성
  boolean online;

  // JPA용 기본 생성자, JPA만 접근할 수 있도록 protected 접근자 설정
  protected UserStatus() {

  }

  public void updateLastConnectAt(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
  }

  public boolean isOnline() {
    online = Duration.between(this.lastActiveAt, Instant.now()).toMinutes() <= 5;
    return online;
  }
}

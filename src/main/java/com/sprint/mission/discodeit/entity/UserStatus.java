package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;


  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  public UserStatus(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
  }

/*
  public UserStatus(UUID userId) {
    this.userId = userId;
    lastActiveAt = Instant.now();
  }
*/


  public void addUser(User user) {
    this.user = user;
  }

  public void updateLastActiveAt(Instant time) {
    this.lastActiveAt = time;
  }

  //마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
  public boolean isOnline() {
    if (Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5) {
      return true;
    } else {
      return false;
    }

  }
}
package com.sprint.mission.discodeit.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import java.time.Duration;
import java.time.Instant;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "user_statuses")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
//사용자의 온라인 상태를 확인하기 위해 활용
public class UserStatus extends BaseUpdatableEntity {

  @JsonBackReference
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(columnDefinition = "timestamp with time zone", nullable = false)
  private Instant lastActiveAt; //사용자의 마지막 접속 시간

  public UserStatus(User user, Instant lastActiveAt) {
    setUser(user);
    this.lastActiveAt = lastActiveAt;
  }

  protected void setUser(User user) {
    this.user = user;
    user.setStatus(this);
  }

  //마지막 접속 시간 수정
  public void updateLastActiveAt(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      updateTimestamp();
    }
  }

  //마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단
  //마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저
  public Boolean isOnline() {
    Instant fiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
    return lastActiveAt.isAfter(fiveMinutesAgo);
  }
}

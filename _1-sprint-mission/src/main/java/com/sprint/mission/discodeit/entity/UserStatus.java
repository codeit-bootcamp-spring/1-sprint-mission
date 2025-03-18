package com.sprint.mission.discodeit.entity;


import jakarta.persistence.CascadeType;
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
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_statuses")
//사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
//사용자의 온라인 상태를 확인하기 위해 활용
public class UserStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user; // 1:1 관계에서 User 엔티티를 참조

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt; //사용자의 마지막 접속 시간

  public UserStatus(User user, Instant lastActiveAt) {
    super();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  //마지막 접속 시간 수정
  public void updateLastActiveAt(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
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

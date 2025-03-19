package com.spirnt.mission.discodeit.enity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spirnt.mission.discodeit.enity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@NoArgsConstructor
@Getter
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne
  @JoinColumn(name = "user_id")
  @JsonIgnoreProperties("status")
  private User user;
  @Column(name = "last_active_at")
  private Instant lastActiveAt; // 마지막으로 확인된 접속 시간

  public UserStatus(User user, UserStatusType type, Instant lastActiveAt) {
    super();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }


  public boolean isOnline() {
    if (lastActiveAt == null) {
      return false; // 접속한 적이 없으면 오프라인으로 간주
    }
    Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
    return lastActiveAt.isAfter(fiveMinutesAgo); // 5분 이내 접속이면 온라인
  }

  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)
        && this.lastActiveAt.isBefore(lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }

  // UUID만으로 객체를 비교하기 위해 추가
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    UserStatus userStatus = (UserStatus) obj;
    return Objects.equals(this.getId(), userStatus.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

}

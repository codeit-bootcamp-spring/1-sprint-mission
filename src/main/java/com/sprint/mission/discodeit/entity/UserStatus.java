package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Entity
@Table(name = "user_statuses")
@Getter
public class UserStatus extends BaseUpdatableEntity /*implements Serializable*/ {

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, unique = true,
      foreignKey = @ForeignKey(name = "fk_userstatus_user"))
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;


  protected UserStatus() {
    super();
  }

  public UserStatus(User user, Instant lastActiveAt) {
    // super(); // 부모의 생성자 호출하여 ID 생성 없어도 호출이 가능하나 명시적으로 작성

    this.user = user;
    this.lastActiveAt = lastActiveAt;

    // 양방향 관계 설정
    if (user != null) {
      user.setStatus(this);
    }
  }


  public void update(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }

  public Boolean isOnline() {
    Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

    return lastActiveAt.isAfter(instantFiveMinutesAgo);
  }
}

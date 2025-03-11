package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

//  private final long ADDITIONAL_TIME_SECONDS = 60 * 5;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(columnDefinition = "timestamp with time zone", nullable = false)
  private Instant lastActiveAt;

  public static UserStatus createUserStatus(User user) {
    return new UserStatus(user);
  }

  private UserStatus(User user) {
    this.user = user;
    this.lastActiveAt = Instant.now();
  }

  public void update(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
//    Instant ValidTime = this.lastActiveAt.plusSeconds(ADDITIONAL_TIME_SECONDS);
//    if (ValidTime.compareTo(Instant.now()) > 0) {
//      this.isOnline = true;
//    } else {
//      this.isOnline = false;
//    }
//    this.updatedAt = Instant.now();
  }
}

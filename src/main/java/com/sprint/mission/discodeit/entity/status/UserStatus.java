package com.sprint.mission.discodeit.entity.status;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_statuses")
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

  @JsonBackReference
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;
  private Instant lastActiveAt;

  public UserStatus(User user) {
    this.user = user;
    user.setStatus(this);
    this.lastActiveAt = Instant.now();
  }

  private static final int USER_ACTIVE_TIMEOUT_SECONDS = 5 * 60;

  public boolean isOnline() {
    return Instant.now().minusSeconds(USER_ACTIVE_TIMEOUT_SECONDS).isBefore(this.getLastActiveAt());
  }
}
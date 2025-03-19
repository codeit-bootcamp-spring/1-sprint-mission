package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;


  public UserStatus(User user, Instant lastActiveAt) {
    this.user = user;
    this.lastActiveAt = lastActiveAt != null ? lastActiveAt : Instant.now();
  }

  public boolean isOnline() {
    return Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5;
  }

  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }
}

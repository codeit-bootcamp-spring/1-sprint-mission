package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.Status.CONNECTED;
import static com.sprint.mission.discodeit.entity.Status.DISCONNECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserStatus extends BaseUpdateEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private UUID id;

  private Instant lastActiveAt;

  @OneToOne(mappedBy = "userStatus")
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public UserStatus(User user, Instant instant) {
    this.user = user;
    this.createdAt = instant;
    this.updatedAt = createdAt;
    this.lastActiveAt = updatedAt;
  }

  public void updateLastActiveAt(Instant instant) {
    this.lastActiveAt = instant;
  }

  public Status getStatus() {
    Instant now = Instant.now();
    if (Duration.between(lastActiveAt, now).getSeconds() <= 300) {
      return CONNECTED;
    } else {
      return DISCONNECTED;
    }
  }
}

package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ReadStatus extends BaseUpdateEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Instant lastReadTime;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User owner;

  @ManyToOne
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  public ReadStatus(User owner, Channel channel, Instant lastReadTime) {
    this.owner = owner;
    this.channel = channel;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
    this.lastReadTime = lastReadTime;
  }

  public void updateLastReadTime(Instant lastReadTime) {
    this.lastReadTime = lastReadTime;
  }
}

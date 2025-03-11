package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "read_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @Column(columnDefinition = "timestamp with time zone", nullable = false)
  private Instant lastReadAt;

  public static ReadStatus createReadStatus(User user, Channel channel, Instant lastReadAt) {
    return new ReadStatus(user, channel, lastReadAt);
  }

  private ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void updateLastReadAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
  }
}

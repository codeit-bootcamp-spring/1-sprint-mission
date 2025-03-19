package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "read_statuses")
@Getter
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_readstatus_user"))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_readstatus_channel"))
  private Channel channel;

  @Column(nullable = false)
  private Instant lastReadAt;

  protected ReadStatus() {
    super();
  }

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;

    // 양방향 관계 설정
    if (user != null) {
      user.addReadStatus(this);
    }
    if (channel != null) {
      channel.addReadStatus(this);
    }
  }

  public void update(Instant newLastReadAt) {
    boolean anyValueUpdated = false;
    if (newLastReadAt != null && !newLastReadAt.equals(this.lastReadAt)) {
      this.lastReadAt = newLastReadAt;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }
}
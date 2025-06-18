package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "read_statuses",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "channel_id"})
    }
)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;


  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;

  @Column(name = "notification_enabled", nullable = false)
  private boolean notificationEnabled;

  //lastReadAt 시간 수정.
  public void updateLastReadAt(Instant time) {
    lastReadAt = time;
  }

  public void updateNotificationEnabled(boolean newValue) {
    this.notificationEnabled = newValue;
  }

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
    this.notificationEnabled = (channel.getChannelType() == ChannelType.PRIVATE); // 기본값 설정
  }

  public ReadStatus(User user, Channel channel, Instant lastReadAt, boolean notificationEnabled) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
    this.notificationEnabled = notificationEnabled;
  }
}
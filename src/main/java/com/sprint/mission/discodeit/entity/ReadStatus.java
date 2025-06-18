package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * - 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현 - 사용자 별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "read_statuses",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_channel", columnNames = {"user_id", "channel_id"})
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user"), nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "channel_id", foreignKey = @ForeignKey(name = "fk_channel"), nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Channel channel;

  @Column(name = "last_read_at", nullable = false)
  private Instant lastReadAt;

  @Column(name = "notification_enabled")
  private boolean notificationEnabled;

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void updateLastReadAt() {    // 읽을 시 시간 갱신을 위함
    this.lastReadAt = Instant.now();
  }

  public void updateLastReadAt(Instant lastReadAt) {    // 읽을 시 시간 갱신을 위함
    this.lastReadAt = lastReadAt;
  }

  public void updateNotificationEnabled(boolean notificationEnabled) {
    this.notificationEnabled = notificationEnabled;
  }

  @PrePersist
  public void setDefaultNotification() {
    if (channel.getChannelType() == ChannelType.PRIVATE) {
      this.notificationEnabled = false;
    } else {
      this.notificationEnabled = true;
    }
  }

  @Override
  public String toString() {
    return "ReadStatus{" +
        "lastReadAt=" + lastReadAt +
        '}';
  }
}

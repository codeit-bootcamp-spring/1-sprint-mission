package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "read_statuses")
@NoArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(columnDefinition = "timestamp with time zone", nullable = false)
  private Instant lastReadAt;

  @Column(nullable = false)
  private boolean notificationEnabled;

  public ReadStatus(Channel channel, User user, Instant lastReadAt, boolean notificationEnabled) {
    this.channel = channel;
    this.user = user;
    this.lastReadAt = lastReadAt;
    this.notificationEnabled = notificationEnabled;
  }
}


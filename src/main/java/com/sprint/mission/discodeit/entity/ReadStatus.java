package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "read_statuses",
    uniqueConstraints = @UniqueConstraint(columnNames = {"channel_id", "user_id"})
)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "last_read_at")
  private Instant lastReadAt;

  @Column
  private boolean notificationEnabled;

  @PrePersist
  private void before() {
    lastReadAt = Instant.now();
  }

  public ReadStatus(Channel channel, User user) {
    this.channel = channel;
    this.user = user;
  }

  public void updateLastReadAtToCurrentTime() {
    lastReadAt = Instant.now();
  }

  public void updateLastReadAt(Instant time) {
    lastReadAt = time;
  }

  public void addChannel(Channel channel) {
    this.channel = channel;
  }

  public void enableNotification() {
    notificationEnabled = true;

  }

  public void disableNotification() {
    notificationEnabled = false;
  }

  public void addUser(User user) {
    this.user = user;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadStatus status = (ReadStatus) o;

    return Objects.equals(getId(), status.getId());

  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}

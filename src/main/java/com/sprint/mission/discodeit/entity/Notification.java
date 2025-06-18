package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "receiver_id", columnDefinition = "uuid")
  private User receiver;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 1000)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column(name = "target_id", columnDefinition = "uuid")
  private UUID targetId;

  public Notification(User receiver, String title, String content,
      NotificationType type, UUID targetId) {
    this.receiver = receiver;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }

  public UUID getReceiverId() {
    return receiver.getId();
  }

  public Instant getCreatedAt() {
    return super.getCreatedAt();
  }
}
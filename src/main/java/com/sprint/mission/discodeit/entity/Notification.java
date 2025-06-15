package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseUpdatableEntity {

  @Column(nullable = false)
  private UUID receiverId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  private UUID targetId;

  @Column(length = 300)
  private String message;

  public Notification(UUID receiverId, NotificationType type, UUID targetId, String message) {
    this.receiverId = receiverId;
    this.type = type;
    this.targetId = targetId;
    this.message = message;
  }
}

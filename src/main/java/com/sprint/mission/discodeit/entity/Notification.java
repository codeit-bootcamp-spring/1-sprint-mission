package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
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
public class Notification extends BaseEntity {

  @Column(nullable = false)
  private UUID receiverId;
  @Column(length = 100, nullable = false)
  private String title;
  @Column(length = 100, nullable = false)
  private String content;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;
  private UUID targetId;

  public Notification(UUID receiverId, String title, String content, NotificationType type,
      UUID targetId) {
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }
}

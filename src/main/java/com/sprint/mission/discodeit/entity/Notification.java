package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "receiver_id")
  private User receiver;

  private String title;

  private String content;

  @Enumerated(EnumType.STRING)
  private NotificationType type;

  private UUID targetId;

  private boolean isRead;

  public Notification(User receiver, NotificationType type, String title, String content,
      UUID targetId) {
    this.receiver = receiver;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
    this.isRead = false;
  }

  public Notification(User receiver, NotificationType type, String title, String content) {
    this.receiver = receiver;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = null;
    this.isRead = false;
  }
}

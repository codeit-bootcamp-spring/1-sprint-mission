package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.configure.NotificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
public class Notification {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User receiver;

  private String message;

  private UUID targetId; // optional

  private Instant createdAt;

  private NotificationType type;

  private String title;

  public Notification(User receiver, String message,
      UUID targetId, NotificationType type,
      String title) {
    this.receiver = receiver;
    this.message = message;
    this.targetId = targetId;
    this.createdAt = Instant.now();
    this.type = type;
    this.title = title;
  }

  protected Notification() {
  }

}

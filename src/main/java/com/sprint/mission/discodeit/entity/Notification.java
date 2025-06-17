package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
public class Notification {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  private User receiverId;

  private String title;
  private String content;

  @Enumerated(EnumType.STRING)
  private NotificationType type;

  private UUID targetId; // optional
  private Instant createdAt;

  public Notification(User receiverId, String title, String content, NotificationType type, UUID targetId, Instant createdAt) {
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
    this.createdAt = createdAt;
  }

  public Notification() {

  }
}


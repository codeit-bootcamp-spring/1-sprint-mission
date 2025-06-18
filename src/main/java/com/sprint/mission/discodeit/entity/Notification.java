package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

  @Column(name = "receiver_id", nullable = false)
  private UUID receiverId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "notification_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  @Column(name = "target_id")
  private UUID targetId;

  @Builder
  private Notification(UUID receiverId, String title, String content,
      NotificationType notificationType, UUID targetId) {
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.notificationType = notificationType;
    this.targetId = targetId;
  }

}

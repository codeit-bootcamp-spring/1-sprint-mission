package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
public class Notification extends BaseEntity {

  @Column(nullable = false)
  private UUID receiverId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private Type type;

  @Column
  private UUID targetId;

  public enum Type {
    NEW_MESSAGE, ROLE_CHANGED, ASYNC_FAILED
  }

  @Builder(access = AccessLevel.PRIVATE)
  private Notification(UUID receiverId, String title, String content, Type type, UUID targetId) {
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }

  public static Notification create(UUID receiverId, String title, String content, Type type, UUID targetId) {
    return Notification.builder()
        .receiverId(receiverId)
        .title(title)
        .content(content)
        .type(type)
        .targetId(targetId)
        .build();
  }

  public Optional<UUID> getTargetId() {
    return Optional.ofNullable(targetId);
  }

  public void updateTargetId(UUID targetId) {
    this.targetId = targetId;
  }



}

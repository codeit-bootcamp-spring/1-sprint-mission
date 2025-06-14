package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "notifications")
@Getter
@AllArgsConstructor
@Builder
public class Notification extends BaseUpdatableEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Column(nullable = false)
  private UUID receiverId;

  @Column
  private UUID targetId;

  protected Notification() {
  }
}

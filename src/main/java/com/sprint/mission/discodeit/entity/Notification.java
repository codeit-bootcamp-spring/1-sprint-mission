package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseUpdatableEntity {

    @Column(name = "receiver_id", columnDefinition = "uuid", nullable = false)
    private UUID receiverId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;
    @Column(name = "target_id", columnDefinition = "uuid")
    private UUID targetId;
    @Column(length = 50)
    private String title;
    @Column(columnDefinition = "text")
    private String content;

    @Builder
    public Notification(UUID receiverId, NotificationType notificationType, UUID targetId, String title,
      String content) {
        this.receiverId = receiverId;
        this.notificationType = notificationType;
        this.targetId = targetId;
        this.title = title;
        this.content = content;
    }
}

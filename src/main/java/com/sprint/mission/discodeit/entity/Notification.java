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

  private static final String NEW_MESSAGE_TITLE = "새로운 메세지가 있습니다.";
  private static final String ROLE_CHANGED_TITLE = "사용자 권한이 수정되었습니다.";
  private static final String ASYNC_FAILED_TITLE = "비동기 요청이 실패했습니다.";

  @Column
  UUID receiverId;
  @Column
  String title;
  @Column
  String content;
  @Enumerated(EnumType.STRING)
  @Column
  NotificationType type;
  @Column
  UUID targetId;

  public Notification(UUID receiverId, String title, String content, NotificationType type,
      UUID targetId) {
    this.receiverId = receiverId;
    this.title = title;
    this.content = content;
    this.type = type;
    this.targetId = targetId;
  }

  public static Notification newMessage(UUID receiverId, String content,
      UUID channelId) {
    return new Notification(receiverId, NEW_MESSAGE_TITLE, content, NotificationType.NEW_MESSAGE,
        channelId);
  }

  public static Notification roleChanged(UUID receiverId, String content,
      UUID userId) {
    return new Notification(receiverId, ROLE_CHANGED_TITLE, content, NotificationType.ROLE_CHANGED,
        userId);
  }

  public static Notification asyncFailed(UUID receiverId, String content) {
    return new Notification(receiverId, ASYNC_FAILED_TITLE, content, NotificationType.ASYNC_FAILED,
        null);
  }

  public enum NotificationType {
    NEW_MESSAGE,
    ROLE_CHANGED,
    ASYNC_FAILED
  }


}

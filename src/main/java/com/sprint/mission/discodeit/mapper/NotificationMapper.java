package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

  public NotificationDto toResponseDto(Notification notification) {
    return NotificationDto.builder()
        .id(notification.getId())
        .createdAt(notification.getCreatedAt())
        .receiverId(notification.getReceiverId())
        .title(notification.getTitle())
        .content(notification.getContent())
        .type(notification.getNotificationType())
        .targetId(notification.getTargetId())
        .build();
  }

}

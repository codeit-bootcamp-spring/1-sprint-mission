package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.NotificationResponse;
import com.sprint.mission.discodeit.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse entityToDto(Notification notification) {
        return NotificationResponse.builder()
            .id(notification.getId())
            .createdAt(notification.getCreatedAt())
            .receiverId(notification.getReceiverId())
            .title(notification.getTitle())
            .content(notification.getContent())
            .type(notification.getType())
            .targetId(notification.getTargetId())
            .build();
    }
}

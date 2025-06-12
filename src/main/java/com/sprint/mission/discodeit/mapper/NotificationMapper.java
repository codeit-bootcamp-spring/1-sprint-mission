package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getCreatedAt(),
                notification.getReceiver().getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getType(),
                notification.getTargetId()
        );
    }
}
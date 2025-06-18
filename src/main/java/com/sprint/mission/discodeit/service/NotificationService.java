package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    List<NotificationDto> findAllNotifications(UUID receiverId);

    void deleteNotification(UUID notificationId, UUID userId);

    CompletableFuture<Void> createNotification(NotificationType notificationType, Channel channel,
        Message message);

    CompletableFuture<Void> createNotification(NotificationType notificationType, User user);

    CompletableFuture<Void> createNotification(NotificationType notificationType, String taskName,
        String username);
}

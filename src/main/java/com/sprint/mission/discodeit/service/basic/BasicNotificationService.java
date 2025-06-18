package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.jpa.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserValidator userValidator;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public List<NotificationDto> findAllNotifications(UUID receiverId) {
        User receiver = userValidator.validateUserExistsByUserId(receiverId);
        List<Notification> notifications = notificationRepository.findAllByReceiver(receiver);

        return notifications.stream().map(notification -> new NotificationDto(
            notification.getId(),
            notification.getCreatedAt(),
            notification.getReceiver().getId(),
            notification.getTitle(),
            notification.getContent(),
            notification.getNotificationType(),
            notification.getTargetId() == null ? null : notification.getTargetId()
        )).collect(Collectors.toList());
    }

    @Override
    public void deleteNotification(UUID notificationId, UUID userId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));

        if (!notification.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("본인의 알람이 아닙니다.");
        }

        notificationRepository.delete(notification);
    }

    @Async
    @Retryable(
        retryFor = {TransientDataAccessException.class, QueryTimeoutException.class},
        maxAttempts = 2,
        backoff = @Backoff
    )
    @Override
    public CompletableFuture<Void> createNotification(NotificationType notificationType,
        Channel channel, Message message) {

        List<ReadStatus> readStatuses = readStatusRepository.findAllReadStatusByChannel(channel);
        readStatuses.forEach(
            readStatus -> {
                Notification notification = new Notification(
                    "메세지가 도착했습니다.",
                    message.getContent(),
                    notificationType,
                    readStatus.getUser(),
                    channel.getId()
                );
                notificationRepository.save(notification);
            }
        );

        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Void> createNotification(NotificationType notificationType,
        User user) {
        Notification notification = new Notification(
            "사용자 권한이 변경되었습니다.",
            user.getRole().toString(),
            notificationType,
            user,
            user.getId()
        );
        notificationRepository.save(notification);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> createNotification(NotificationType notificationType,
        String taskName, String username) {
        User user = userValidator.validateUserExistsByUserName(username);

        Notification notification = new Notification(
            "비동기 작업이 실패했습니다.",
            taskName,
            notificationType,
            user,
            null
        );
        notificationRepository.save(notification);

        return CompletableFuture.completedFuture(null);
    }
}

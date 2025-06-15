package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.NotificationResponse;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationResponse> getMyNotifications(UUID receiverId) {
        return notificationRepository.getAllByReceiverId(receiverId).stream()
            .map(notificationMapper::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public void readNotification(UUID id, UUID receiverId) {
        notificationRepository.deleteByIdAndReceiverId(id, receiverId);
    }

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
        value = TransientDataAccessException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void createNotification(NotificationEvent notificationEvent) {
        log.info("이벤트 발행 확인: {} / {}", notificationEvent.getType(),
            notificationEvent.getReceiverId());
        Notification notification = Notification.create(
            notificationEvent.getReceiverId(),
            notificationEvent.getTitle(),
            notificationEvent.getContent(),
            notificationEvent.getType(),
            notificationEvent.getTargetId());

        notificationRepository.save(notification);
    }

    @Recover
    public void recover(Exception e, NotificationEvent notificationEvent) {
        log.error("알림 전송 재시도 실패 : {}", notificationEvent.getReceiverId(), e);
    }
}

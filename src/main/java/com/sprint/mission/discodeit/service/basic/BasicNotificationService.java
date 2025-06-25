package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.NotificationResponse;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.event.NotificationCreatedEvent;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "notifications", key = "#receiverId", unless = "#result.isEmpty()")
    @Override
    public List<NotificationResponse> getMyNotifications(UUID receiverId) {
        return notificationRepository.getAllByReceiverId(receiverId).stream()
            .map(notificationMapper::entityToDto)
            .collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "notifications", key = "#receiverId")
    @Transactional
    @Override
    public void readNotification(UUID id, UUID receiverId) {
        notificationRepository.deleteByIdAndReceiverId(id, receiverId);
    }

    @Transactional
    @Retryable(
        value = TransientDataAccessException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @Override
    public void createNotification(NotificationEvent notificationEvent) {
        UUID userId = notificationEvent.getReceiverId();

        log.info("이벤트 발행 확인: {} / {}", notificationEvent.getType(), userId);
        Notification notification = Notification.create(
            userId,
            notificationEvent.getTitle(),
            notificationEvent.getContent(),
            notificationEvent.getType(),
            notificationEvent.getTargetId());

        Notification savedNotification = notificationRepository.save(notification);
        NotificationResponse response = notificationMapper.entityToDto(savedNotification);

        Objects.requireNonNull(cacheManager.getCache("notifications"))
            .evict(notificationEvent.getReceiverId());

        eventPublisher.publishEvent(new NotificationCreatedEvent(userId, response));
    }

    @Recover
    public void recover(TransientDataAccessException e, NotificationEvent notificationEvent) {
        log.error("알림 전송 재시도 실패 : {}", notificationEvent.getReceiverId(), e);
    }
}

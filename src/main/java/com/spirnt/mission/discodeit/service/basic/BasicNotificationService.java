package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.async.notification.NotificationCreateEvent;
import com.spirnt.mission.discodeit.cache.event.NotificationEvictEvent;
import com.spirnt.mission.discodeit.dto.notification.NotificationDto;
import com.spirnt.mission.discodeit.entity.Notification;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.DiscodeitException;
import com.spirnt.mission.discodeit.exception.ErrorCode;
import com.spirnt.mission.discodeit.mapper.NotificationMapper;
import com.spirnt.mission.discodeit.repository.NotificationRepository;
import com.spirnt.mission.discodeit.service.NotificationService;
import com.spirnt.mission.discodeit.sse.SseEmitterManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final SseEmitterManager emitterManager;

    @Transactional
    public List<NotificationDto> create(NotificationCreateEvent event) {
        List<Notification> notifications = event.receivers().stream()
            .map(user -> new Notification(user, event.title(), event.content(), event.type(),
                event.targetId()))
            .collect(Collectors.toList());
        notificationRepository.saveAll(notifications);

        // 캐시 무효화를 위한 이벤트 발행
        eventPublisher.publishEvent(
            new NotificationEvictEvent(event.receivers().stream()
                .map(User::getId)
                .collect(Collectors.toList()))
        );

        // sse로 실시간 알림 전송
        for (Notification notification : notifications) {
            emitterManager.sendNotification(notification.getReceiver().getId(),
                notificationMapper.toDto(notification));
        }

        return notificationMapper.toDto(notifications);
    }

    @Cacheable(
        cacheNames = "notifications",
        key = "#userId"
    )
    @Transactional(readOnly = true)
    @Override
    public List<NotificationDto> findAll(UUID userId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);
        return notificationMapper.toDto(notifications);
    }

    @Transactional
    @Override
    public void delete(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.NOTIFICATION_NOT_FOUND, Map.of()));

        // 캐시 무효화를 위한 이벤트 발행
        eventPublisher.publishEvent(
            new NotificationEvictEvent(List.of(notification.getReceiver().getId()))
        );

        notificationRepository.delete(notification);
    }


}

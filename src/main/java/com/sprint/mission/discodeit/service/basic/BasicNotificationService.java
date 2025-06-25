package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.notification.NotificationEvent;
import com.sprint.mission.discodeit.repository.jpa.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;


    @Cacheable(cacheNames = "notificationsByUser", key = "#receiverId")
    @Transactional(readOnly = true)
    public List<NotificationDto> findAllByReceiver(UUID receiverId) {
        return notificationRepository.findAllByReceiver_IdOrderByCreatedAtDesc(receiverId).stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Transactional
    public void delete(UUID notificationId, UUID currentUserId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        if (!notification.getReceiver().getId().equals(currentUserId)) {
            throw new AuthorizationDeniedException("본인의 알림만 삭제할 수 있습니다.");
        }

        notificationRepository.delete(notification);
    }


    //이 메서드는 실제로 Notification을 DB에 저장하는 역할을 해요.
    //재시도 설정 (실패 시 자동 재시도하고 싶다면)
    @Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    @CacheEvict(cacheNames = "notificationsByUser", key = "#event.receiverId")
    @Transactional
    public Notification send(NotificationEvent event) {
        Notification notification = Notification.builder()
                .receiver(User.withId(event.receiverId())) // User.withId()는 직접 만든 정적 팩토리 메서드 (프록시 생성용)
                .title(event.title())
                .content(event.content())
                .type(event.type())
                .targetId(event.targetId())
                .build();

        return notificationRepository.save(notification);
    }
}
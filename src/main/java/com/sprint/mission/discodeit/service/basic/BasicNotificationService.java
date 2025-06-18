package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.CacheConfig;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final CacheManager cacheManager;

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.USER_NOTIFICATIONS, key = "#receiverId")
    @Override
    public List<NotificationDto> findAllByReceiverId(UUID receiverId) {
        log.debug("사용자별 알림 목록 조회 시작: receiverId={}", receiverId);
        List<NotificationDto> notifications = notificationRepository
            .findAllByReceiverIdOrderByCreatedAtDesc(receiverId)
            .stream()
            .map(notificationMapper::toDto)
            .toList();

        List<NotificationDto> result = new ArrayList<>(notifications);
        log.info("사용자별 알림 목록 조회 완료: receiverId={}, 알림 수={}", receiverId, result.size());
        return result;
    }

    @Transactional
    @Override
    public void delete(UUID notificationId, UUID requesterId) {
        log.debug("알림 삭제 시작: notificationId={}, requesterId={}", notificationId, requesterId);

        Notification notification = notificationRepository.findByIdWithReceiver(notificationId)
            .orElseThrow(() -> {
                DiscodeitException exception = new DiscodeitException(
                    ErrorCode.NOTIFICATION_NOT_FOUND);
                exception.addDetail("notificationId", notificationId);
                return exception;
            });

        if (!notification.getReceiver().getId().equals(requesterId)) {
            DiscodeitException exception = new DiscodeitException(ErrorCode.INVALID_REQUEST);
            exception.addDetail("reason", "Cannot delete other user's notification");
            exception.addDetail("notificationId", notificationId);
            exception.addDetail("receiverId", notification.getReceiver().getId());
            throw exception;
        }

        UUID receiverId = notification.getReceiver().getId();
        notificationRepository.delete(notification);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictUserNotificationsCache(receiverId);
                    log.info("알림 삭제 완료: notificationId={}, receiverId={} - 트랜잭션 커밋 후 알림 캐시 무효화",
                        notificationId, receiverId);
                }
            }
        );

        log.info("알림 삭제 완료: notificationId={}, receiverId={} - 알림 캐시 무효화", notificationId,
            receiverId);
    }

    private void evictUserNotificationsCache(UUID receiverId) {
        try {
            Cache cache = cacheManager.getCache(CacheConfig.USER_NOTIFICATIONS);
            if (cache != null) {
                cache.evict(receiverId);
                log.debug("사용자 알림 캐시 무효화 완료: receiverId={}", receiverId);
            } else {
                log.warn("알림 캐시를 찾을 수 없음: cacheName={}", CacheConfig.USER_NOTIFICATIONS);
            }
        } catch (Exception e) {
            log.error("알림 캐시 무효화 실패: receiverId={}", receiverId, e);
        }
    }
}

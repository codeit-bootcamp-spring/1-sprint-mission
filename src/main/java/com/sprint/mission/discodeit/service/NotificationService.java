package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Cacheable(value = "userNotifications", key = "#userId")
    @Transactional(readOnly = true)
    public List<NotificationDto> getUserNotifications(UUID userId) {
        System.out.println("알림 개수: " + notificationRepository.findAllByReceiver_Id(userId).size());
        return notificationRepository.findAllByReceiver_Id(userId)
                .stream().map(notificationMapper::toDto).toList();
    }

    @CacheEvict(value = "userNotifications", key = "#userId")
    @Transactional
    public void deletedIfOwner(UUID notificationId, UUID userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        if (!notification.getReceiver().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 알림만 삭제 가능");
        }
        notificationRepository.delete(notification);
    }
}

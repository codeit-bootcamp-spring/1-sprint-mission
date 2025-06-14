package com.sprint.mission.discodeit.event;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sprint.mission.discodeit.dto.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.dto.event.UserRoleUpdateEvent;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

	private final ReadStatusRepository readStatusRepository;
	private final NotificationRepository notificationRepository;

	@Async
	@Retryable(
		retryFor = RuntimeException.class,
		maxAttempts = 3,
		backoff = @Backoff(delay = 2000, multiplier = 2)
	)
	@CacheEvict(cacheNames = "notificationList", key = "#event.channelId()")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleMessageCreatedEvent(MessageCreatedEvent event) {
		log.info("알림 생성 시작: {}", event);
		UUID channelId = event.channelId();
		String messageContent = event.messageContent();
		List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelIdWithUser(channelId);
		List<Notification> notifications = readStatuses.stream()
			.filter(ReadStatus::isNotificationEnabled)
			.map(readStatus -> Notification.builder()
				.receiverId(readStatus.getUser().getId())
				.notificationType(NotificationType.NEW_MESSAGE)
				.targetId(channelId)
				.title("새 메시지가 도착했습니다.")
				.content(messageContent)
				.build()
			).toList();
		notificationRepository.saveAll(notifications);
		log.info("알림 생성 완료.");
	}

	@Recover
	public void recoverMessageNotificationCreation(RuntimeException e, MessageCreatedEvent event) {
		log.error("메시지 알림 생성 실패. 재시도 횟수 초과. 채널 ID: {}", event.channelId(), e);
	}

	@Async
	@Retryable(
		retryFor = RuntimeException.class,
		maxAttempts = 3,
		backoff = @Backoff(delay = 2000, multiplier = 2)
	)
	@CacheEvict(cacheNames = "notificationList", key = "#event.receivedId()")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUserRoleUpdateEvent(UserRoleUpdateEvent event) {
		log.info("알림 생성 시작 : {}", event);
		String title = "권한 변경 알림";
		String content = "당신의 권한이 " + event.role().name() + "으로 변경되었습니다.";

		Notification notification = Notification.builder()
			.receiverId(event.receivedId())
			.notificationType(NotificationType.ROLE_CHANGED)
			.targetId(null)
			.title(title)
			.content(content)
			.build();
		notificationRepository.save(notification);
		log.info("알림 생성 완료");
	}

	@Recover
	public void recoverUserRoleNotification(RuntimeException e, UserRoleUpdateEvent event) {
		log.error("권한 변경 알림 생성 실패 - 재시도 모두 실패: userId={}, role={}",
			event.receivedId(), event.role(), e);
	}
}

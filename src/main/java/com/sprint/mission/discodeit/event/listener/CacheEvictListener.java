package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.event.MultipleNotificationCreatedEvent;
import com.sprint.mission.discodeit.event.PrivateChannelCreatedEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheEvictListener {

  private final CacheManager cacheManager;

  @Async("eventTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(PrivateChannelCreatedEvent event) {
    log.debug("PRIVATE 채널 생성 이벤트 처리 시작: channelId={}", event.channel().id());

    Set<UUID> receiverIds = new HashSet<>(event.participantIds());
    Cache cache = cacheManager.getCache("channelsByUser");

    if (cache != null) {
      receiverIds.forEach(receiverId -> {
        cache.evict(receiverId);
        log.debug("캐시 무효화 완료: receiverId={}", receiverId);
      });
    }

    log.info("PRIVATE 채널 생성 이벤트 처리 완료: receiverIds={}", receiverIds);
  }

  @Async("eventTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(MultipleNotificationCreatedEvent event) {
    log.debug("알림 생성 이벤트 처리 시작: receiverIds={}", event.receiverIds());

    Set<UUID> receiverIds = event.receiverIds();
    Cache cache = cacheManager.getCache("notificationsByUser");

    if (cache != null) {
      receiverIds.forEach(receiverId -> {
        cache.evict(receiverId);
        log.debug("캐시 무효화 완료: receiverId={}", receiverId);
      });
    }

    log.info("알림 생성 이벤트 처리 완료: receiverIds={}", receiverIds);
  }
}
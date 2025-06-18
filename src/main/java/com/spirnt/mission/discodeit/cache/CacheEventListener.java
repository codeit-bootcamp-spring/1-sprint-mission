package com.spirnt.mission.discodeit.cache;

import com.spirnt.mission.discodeit.cache.event.ChannelDeleteEvent;
import com.spirnt.mission.discodeit.cache.event.NotificationEvictEvent;
import com.spirnt.mission.discodeit.cache.event.PrivateChannelCreateEvent;
import com.spirnt.mission.discodeit.entity.ChannelType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CacheEventListener {

    private final CacheManager cacheManager;


    // 프라이빗 채널이 생성되면 참여하는 사용자들에 대해 사용자별 채널 목록 조회를 캐시에서 제거해야 함.
    // 그러나, @CacheEvict으로는 단일 키 또는 전체 엔트리로만
    // 설정 가능해서 이벤트로 분리
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPrivateChannelCreate(PrivateChannelCreateEvent event) {
        Cache cache = cacheManager.getCache("channels");
        event.participantIds().forEach(userId -> cache.evict(userId));
    }

    // 채널 삭제 시 채널 목록 조회 캐시에서 삭제
    // 퍼블릭 채널이면 모든 엔트리 무효화
    // 프라이빗 채널이면 채널 참가한 사용자 키만 무효화
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onChannelDelete(ChannelDeleteEvent event) {
        Cache cache = cacheManager.getCache("channels");
        if (event.channelType().equals(ChannelType.PUBLIC)) {
            cache.clear();
        } else {
            event.participantIds().forEach(userId -> cache.evict(userId));
        }
    }

    // 알림이 생성되거나 삭제될 때
    // 유저별로 캐시된 알림 목록 조회 데이터를 무효화
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNotificationChange(NotificationEvictEvent event) {
        Cache cache = cacheManager.getCache("notifications");
        event.userIds().forEach(userId -> cache.evict(userId));
    }
}

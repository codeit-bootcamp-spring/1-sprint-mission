package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.config.CacheName;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.event.PrivateChannelCreatedEvent;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheEventListener {

  private final CacheManager cacheManager;

  @Async("eventExecutor")
  @TransactionalEventListener
  public void handle(PrivateChannelCreatedEvent event) {
    ChannelDto channelDto = event.channelDto();
    List<UUID> participantIds = event.participantIds();

    Cache cache = cacheManager.getCache(CacheName.CHANNELS_BY_USER);
    if (cache != null) {
      participantIds.forEach(cache::evict);
    }
  }
}

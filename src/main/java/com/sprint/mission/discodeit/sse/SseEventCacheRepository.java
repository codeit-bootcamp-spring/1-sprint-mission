package com.sprint.mission.discodeit.sse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Component
public class SseEventCacheRepository {

    private final Map<UUID, Map<String, SseEventBuilder>> eventCache = new ConcurrentHashMap<>();

    public void save(UUID userId, String eventId, SseEventBuilder event) {
        eventCache.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(eventId, event);
    }

    public Map<String, SseEventBuilder> getEventsAfter(UUID userId, String lastEventId) {
        return eventCache.getOrDefault(userId, Map.of()).entrySet().stream()
                .filter(e -> e.getKey().compareTo(lastEventId) > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void clear(UUID userId) {
        eventCache.remove(userId);
    }
}

package com.sprint.mission.discodeit.sse;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterRepository {

    private final Map<UUID, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void add(UUID userId, String emitterId, SseEmitter sseEmitter) {
        emitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .put(emitterId, sseEmitter);
    }

    public void remove(UUID userId, String emitterId) {
        Map<String, SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitterId);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

    public Collection<SseEmitter> getEmitters(UUID userId) {
        return emitters.getOrDefault(userId, Map.of()).values();
    }

    public Map<UUID, Map<String, SseEmitter>> getAll() {
        return emitters;
    }
}

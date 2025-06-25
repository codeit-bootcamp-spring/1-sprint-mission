package com.sprint.mission.discodeit.service;

import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

    SseEmitter createConnection(UUID userId, String lastEventId);

    void sendToUser(String eventName, Object data, UUID userId);

    void sendToAll(String eventName, Object data);

    void sendBroadcastPing();
}

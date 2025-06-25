package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.NotificationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseEmitterService {
    private final long timeOut=60*1000L*5;
    private final Map<UUID, List<SseEmitter>> emitters=new ConcurrentHashMap<>();

    public SseEmitter connect(UUID userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(timeOut);
        emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError(e -> removeEmitter(userId, emitter));
        return emitter;
    }

    public void send(UUID userId, Object data) {
        List<SseEmitter> emitterList = emitters.getOrDefault(userId, List.of());
        List<SseEmitter> deadEmitters=new ArrayList<>();
        for (SseEmitter emitter : emitterList) {
            try {
                String eventId = UUID.randomUUID().toString();
                emitter.send(SseEmitter.event()
                        .id(eventId)
                        .name("message")
                        .data(data));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
            emitterList.removeAll(deadEmitters);
        }
    }
    public void sendPingToAll(){
        for (UUID userId : emitters.keySet()) {
            send(userId,"ping");
        }
    }

    private void removeEmitter(UUID userId, SseEmitter emitter) {
        List<SseEmitter> emitterList = emitters.get(userId);
        if(emitterList!=null) {
            emitterList.remove(emitter);
            if(emitterList.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

    public void sendEvent(UUID userId, String eventName, Object data) {
        List<SseEmitter> emitterList = emitters.getOrDefault(userId, List.of());
        List<SseEmitter> deadEmitters=new ArrayList<>();
        for (SseEmitter emitter : emitterList) {
            try {
                String eventId = UUID.randomUUID().toString();
                emitter.send(SseEmitter.event()
                        .id(eventId)
                        .name(eventName)
                        .data(data));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        }
        emitterList.removeAll(deadEmitters);
    }

    public void sendNotification(UUID userId, NotificationDto notification) {
        sendEvent(userId,"notifications",notification);
    }

    public void sendBinaryContentStatus(UUID userId, BinaryContentDto contentDto) {
        sendEvent(userId,"binaryContents.status",contentDto);
    }
    public void sendChannelRefresh(UUID userId,UUID channelId) {
        Map<String, String> data = Map.of("channelId", channelId.toString());
        sendEvent(userId,"channels.refresh",data);
    }

    public void sendUserRefresh(UUID userId, UUID targetUserId) {
        Map<String, String> data = Map.of("userId", targetUserId.toString());
        sendEvent(userId,"users.refresh",data);
    }
}

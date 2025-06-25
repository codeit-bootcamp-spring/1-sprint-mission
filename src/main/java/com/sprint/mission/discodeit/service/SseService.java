package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

  void sendInitEvent(SseEmitter emitter, String lastEventId);

  void safeSend(SseEmitter emitter, SseEmitter.SseEventBuilder event, Long userId);

  void sendNotification(Long userId, NotificationDto dto);

}

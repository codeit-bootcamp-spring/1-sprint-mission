package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

  SseEmitter createConnection(UUID userId, String lastEventId);

  void sendNotificationEvent(UUID userId, NotificationDto notification);

  void sendBinaryContentStatusEvent(UUID userId, BinaryContentDto binaryContent);

  void sendChannelRefreshEvent(UUID userId, UUID channelId);

  void sendUserRefreshEvent(UUID userId, UUID targetUserId);
  
}

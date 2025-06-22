package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SshService {

  SseEmitter subscribe(UUID userId, UUID lastEventId);

  void sendNotification(UUID userId, NotificationDto dto);

  void sendBinaryContentStatus(UUID userId, BinaryContentDto dto);

  void sendChannelRefresh(UUID userId, UUID channelId);

  void sendUserRefresh(UUID userId);

  void broadcast(UUID userId, String eventName, Object data);

  void ping();

}

package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import java.util.List;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {

  SseEmitter subscribe(UUID userId, String lastEventId);

  void send(UUID userId, String eventName, Object data);

  void sendNotification(UUID userId, NotificationDto notification);

  void sendBinaryContentStatus(UUID userId, BinaryContentResponseDto binaryContent);

  void sendChannelRefresh(UUID userId, UUID channelId);

  void sendChannelRefreshToIdList(List<UUID> userIds, UUID channelId);

  void sendUserRefresh(UUID userId);

  void sendPing();

}

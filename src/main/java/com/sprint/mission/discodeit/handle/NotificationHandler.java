package com.sprint.mission.discodeit.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.NotificatonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationHandler {
  private final AsyncNotificationSender asyncNotificationSender;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "discodeit.notification", groupId = "discodeit-group")
  public void consume(String message) {
    try {
      NotificatonDto dto = objectMapper.readValue(message, NotificatonDto.class);
      asyncNotificationSender.send(dto);
    } catch (JsonProcessingException e) {
      log.error("Kafka 역직렬화 실패", e);
    }
  }
}

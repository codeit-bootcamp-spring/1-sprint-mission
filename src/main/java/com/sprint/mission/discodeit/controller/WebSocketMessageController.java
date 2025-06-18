package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {

  private final MessageService messageService;
  private final SimpMessagingTemplate messagingTemplate;

  // 첨부파일 없는 텍스트 메시지만 WebSocket 으로 처리
  @MessageMapping("/messages") // /pub/messages 로 온 요청 처리
  public void sendTextMessage(MessageCreateRequest request) {
    log.info("WebSocket 텍스트 메시지 생성 요청: {}", request);

    try {
      MessageDto messageDto = messageService.create(request);

      // 해당 채널을 구독한 모든 클라이언트에게 전송
      messagingTemplate.convertAndSend(
          "/sub/channels." + request.getChannelId() + ".messages", messageDto
      );
    } catch (Exception e) {
      log.error("WebSocket 메시지 전송 실패: {}", e.getMessage(), e);
    }
  }
}

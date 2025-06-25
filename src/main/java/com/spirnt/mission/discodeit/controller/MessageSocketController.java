package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 첨부파일이 없는 텍스트 메시지
    @MessageMapping("/messages")
    public void handleTextMessage(MessageCreateRequest request) {
        log.info("[WebSocket] 메시지 수신: {}", request);

        // 첨부파일 없는 메시지 저장
        MessageDto message = messageService.create(request, null);

        // 해당 채널을 구독 중인 사용자에게 메시지 전송
        String destination = "/sub/channels." + request.channelId() + ".messages";
        simpMessagingTemplate.convertAndSend(destination, message);
    }
}

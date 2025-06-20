package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

  private final UserRepository userRepository;
  private final SimpMessagingTemplate template; // 특정 사용자에게 메시지를 송신하는데 사용하는 STOMP 템플릿
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;
  private final MessageRepository messageRepository;
  private final MessageService messageService;

  // (1) 클라이언트(발행자)의 메시지 송신 /pub/messages
  @MessageMapping("/messages")
  public void sendMassage(MessageCreateRequest request) {
    log.info("발행자가 메세지 송신 : authorId={}, content={}", request.authorId(), request.content());
    
    MessageDto messageDto = messageService.createMessage(request, null);

    // (2) 다른 클라이언트 들(구독자)에게 메세지 수신
    // Subscribe 엔드 포인트 : /sub/channels.{channelId}.messages
    String destination = "/sub/channels." + request.channelId() + ".messages";
    log.info("구독자에게 메세지 수신 시도 : destination={}, content={}", destination, messageDto.content());
    template.convertAndSend(destination, messageDto);
  }

}

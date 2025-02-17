package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.service.facade.MessageMasterFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/channel")
public class MessageController {

  private final MessageMasterFacade messageMasterFacade;

  @RequestMapping("/{channelId}/message")
  public ResponseEntity<MessageResponseDto> sendMessage(@PathVariable String channelId, @ModelAttribute CreateMessageDto messageDto){
    MessageResponseDto message = messageMasterFacade.createMessage(messageDto, channelId);
    return ResponseEntity.ok(message);
  }
}

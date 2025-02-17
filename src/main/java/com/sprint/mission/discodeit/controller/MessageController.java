package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.facade.MessageMasterFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MessageController {

  private final MessageMasterFacade messageMasterFacade;

  @RequestMapping(value = "/channel/{channelId}/message", method = RequestMethod.POST)
  public ResponseEntity<MessageResponseDto> sendMessage(@PathVariable String channelId, @ModelAttribute CreateMessageDto messageDto){
    MessageResponseDto message = messageMasterFacade.createMessage(messageDto, channelId);
    return ResponseEntity.ok(message);
  }

  @RequestMapping(value = "/message/{messageId}", method = RequestMethod.PUT)
  public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable String messageId, @ModelAttribute MessageUpdateDto messageDto){
    MessageResponseDto message = messageMasterFacade.updateMessage(messageId, messageDto  );
    return ResponseEntity.ok(message);
  }
}

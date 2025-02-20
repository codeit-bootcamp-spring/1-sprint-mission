package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.service.facade.message.MessageMasterFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MessageController {

  private final MessageMasterFacade messageMasterFacade;

  @PostMapping("/channels/{channelId}/messages")
  public ResponseEntity<MessageResponseDto> sendMessage(@PathVariable String channelId, @ModelAttribute CreateMessageDto messageDto){
    MessageResponseDto message = messageMasterFacade.createMessage(messageDto, channelId);
    return ResponseEntity.ok(message);
  }


  @PatchMapping("/messages/{messageId}")
  public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable String messageId, @ModelAttribute MessageUpdateDto messageDto){
    MessageResponseDto message = messageMasterFacade.updateMessage(messageId, messageDto  );
    return ResponseEntity.ok(message);
  }

  @DeleteMapping("/messages/{messageId}")
  public ResponseEntity<String> deleteMessage(@PathVariable String messageId){
    messageMasterFacade.deleteMessage(messageId);
    return ResponseEntity.ok("successfully deleted");
  }

  @GetMapping("/channels/{channelId}/messages")
  public ResponseEntity<List<MessageResponseDto>> getChannelMessages(@PathVariable String channelId){
    List<MessageResponseDto> messages = messageMasterFacade.findMessagesByChannel(channelId);
    return ResponseEntity.ok(messages);
  }
}

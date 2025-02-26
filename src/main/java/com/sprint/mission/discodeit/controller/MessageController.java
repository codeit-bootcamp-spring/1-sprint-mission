package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Message", description = "메세지 API")
public class MessageController {

  private final MessageService messageService;

  @GetMapping("/all")
  public List<MessageResponseDto> getAllMessages() {
    return messageService.findAll();
  }

  //특정 채널 메세지 생성
  @PostMapping
  public MessageResponseDto createMessage(@RequestBody CreateMessageDto createMessageDto) {
    return messageService.create(createMessageDto);
  }

  //특정 채널 메세지 수정
  @PutMapping("/{messageId}")
  public MessageResponseDto updateMessage(@PathVariable String messageId,
      @RequestBody UpdateMessageDto updateMessageDto) {
    return messageService.updateMessage(messageId, updateMessageDto);
  }

  //특정 사용자의 모든 메세지 목록 조회
  @GetMapping("/users/{userId}")
  public List<MessageResponseDto> getMessagesByUserId(@PathVariable String userId) {
    return messageService.findAllBySenderId(userId);
  }

  //특정 채널의 모든 메세지 목록 조회
  @GetMapping("/channels/{channelId}")
  public List<MessageResponseDto> getMessagesByChannelId(@PathVariable String channelId) {
    return messageService.findAllByChannelId(channelId);
  }

  //메세지 삭제
  @DeleteMapping("/{messageId}")
  public String deleteMessage(@PathVariable String messageId, @RequestParam String userId) {
    if (messageService.delete(messageId, userId)) {
      return "Message deleted successfully";
    }
    return "Message not found";
  }
}

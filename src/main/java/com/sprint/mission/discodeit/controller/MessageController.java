package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  // 메시지 전송
  @PostMapping
  public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageCreateDTO messageCreateDTO) {
    return ResponseEntity.ok(messageService.createMessage(messageCreateDTO));
  }

  // 메시지 수정
  @PatchMapping("/{id}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable("id") UUID id,
      @RequestBody MessageUpdateDTO messageUpdateDTO) {

    return ResponseEntity.ok(messageService.update(messageUpdateDTO));
  }

  // 메시지 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteMessage(@PathVariable("id") UUID id) {
    messageService.deleteMessage(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Message deleted");
  }

  // 채널 ID로 메시지 목록 조회
  @GetMapping("/{id}")
  public ResponseEntity<List<MessageDto>> getMessagesByChannelId(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(messageService.findAllByChannelId(id)); //
  }
}

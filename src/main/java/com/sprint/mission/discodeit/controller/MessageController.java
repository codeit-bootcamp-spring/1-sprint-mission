package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
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
  public ResponseEntity<String> sendMessage(@RequestBody MessageCreateDTO messageCreateDTO) {
    messageService.createMessage(messageCreateDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("Message sent: " + messageCreateDTO.toString());
  }

  // 메시지 수정
  @PatchMapping("/{id}")
  public ResponseEntity<String> updateMessage(@PathVariable("id") UUID id,
      @RequestBody MessageUpdateDTO messageUpdateDTO) {
    messageService.update(messageUpdateDTO);
    return ResponseEntity.status(HttpStatus.OK)
        .body("Message updated: " + messageUpdateDTO.toString());
  }

  // 메시지 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteMessage(@PathVariable("id") UUID id) {
    messageService.deleteMessage(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Message deleted");
  }

  // 채널 ID로 메시지 목록 조회
  @GetMapping("/{id}")
  public ResponseEntity<List<Message>> getMessagesByChannelId(@PathVariable("id") UUID id) {
    List<Message> messages = messageService.findAllByChannelId(id);
    return ResponseEntity.ok(messages); // 상태 코드 200과 함께 메시지 목록 반환
  }
}

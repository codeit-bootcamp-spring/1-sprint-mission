package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResponseDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/messages")
public class MessageController {

  private final MessageService messageService;

  @PostMapping
  public ResponseDTO<Message> create(@ModelAttribute MessageCreateDTO request) {
    return ResponseDTO.<Message>builder()
        .code(HttpStatus.CREATED.value())
        .message("메시지 생성 완료")
        .data(messageService.create(request))
        .build();
  }

  @PutMapping("{messageId}")
  public ResponseDTO<Message> update(@PathVariable UUID messageId,
      @RequestBody MessageUpdateDTO request) {
    return ResponseDTO.<Message>builder()
        .code(HttpStatus.OK.value())
        .message("메시지 수정 완료")
        .data(messageService.update(messageId, request))
        .build();
  }

  @DeleteMapping("{messageId}")
  public ResponseDTO<UUID> delete(@PathVariable UUID messageId) {
    return ResponseDTO.<UUID>builder()
        .code(HttpStatus.NO_CONTENT.value())
        .message("메시지가 삭제 완료")
        .data(messageService.delete(messageId))
        .build();
  }

  @GetMapping
  public ResponseDTO<List<Message>> findAllByChannelId(@RequestParam("channelId") UUID channelId) {
    return ResponseDTO.<List<Message>>builder()
        .code(HttpStatus.OK.value())
        .message("특정 채널 메시지 조회")
        .data(messageService.findAllByChannelId(channelId))
        .build();
  }


}

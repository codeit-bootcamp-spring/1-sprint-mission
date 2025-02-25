package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
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
  public ApiResponse<UUID> sendMessage(@ModelAttribute MessageCreateDTO request) {
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.CREATED.value())
        .message("메시지 생성 완료")
        .data(messageService.create(request))
        .build();
  }

  @PutMapping("{messageId}")
  public ApiResponse updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateDTO request) {
    messageService.update(messageId, request);
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.OK.value())
        .message("메시지 수정 완료")
        .build();
  }

  @DeleteMapping("{messageId}")
  public ApiResponse<UUID> deleteMessage(@PathVariable UUID messageId) {
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.NO_CONTENT.value())
        .message("메시지가 삭제 완료")
        .data(messageService.delete(messageId))
        .build();
  }

  @GetMapping
  public ApiResponse<List<Message>> getMessageByChannel(@RequestParam("channelId") UUID channelId) {
    return ApiResponse.<List<Message>>builder()
        .code(HttpStatus.OK.value())
        .message("특정 채널 메시지 조회")
        .data(messageService.findAllByChannelId(channelId))
        .build();
  }


}

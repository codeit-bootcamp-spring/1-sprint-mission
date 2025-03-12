package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.awt.print.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Message", description = "메세지 API")
public class MessageController {

  private final MessageService messageService;

  //특정 채널 메세지 생성
  @PostMapping
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments,
      @RequestPart(value = "messageCreateRequest") CreateMessageDto createMessageDto) {

    MessageDto messageDto;

    if (attachments != null && !attachments.isEmpty()) {
      messageDto = messageService.create(createMessageDto, attachments);
    } else {
      messageDto = messageService.create(createMessageDto);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
  }

  //특정 채널 메세지 수정
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable String messageId,
      @RequestBody UpdateMessageDto updateMessageDto) {
    return ResponseEntity.ok(messageService.updateMessage(messageId, updateMessageDto));
  }

  //특정 채널의 최근 50개 메세지 조회
  @GetMapping
  public ResponseEntity<List<MessageDto>> getAllMessages(@RequestParam String channelId,
      @RequestParam Pageable pageable) {
    PageResponse<MessageDto> allByChannelIdWithPaging = messageService.findAllByChannelIdWithPaging(
        channelId, pageable);

    return ResponseEntity.ok().body(allByChannelIdWithPaging.getContents());
  }

  //메세지 삭제
  @DeleteMapping("/{messageId}")
  public ResponseEntity<String> deleteMessage(@PathVariable String messageId,
      @RequestParam String userId) {
    if (messageService.delete(messageId, userId)) {
      return ResponseEntity.ok().body("Message (id=" + messageId + ") deleted");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }
}

package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
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
      @Valid @RequestPart(value = "messageCreateRequest") CreateMessageDto createMessageDto) {

    log.info("메세지 생성 요청: {}", createMessageDto);
    try {
      MessageDto messageDto;

      if (attachments != null && !attachments.isEmpty()) {
        messageDto = messageService.create(createMessageDto, attachments);
      } else {
        messageDto = messageService.create(createMessageDto);
      }
      return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }

  }

  //특정 채널 메세지 수정
  //@PreAuthorize("hasRole('ADMIN') or #updateMessageDto.userId() == authentication.principal.user.id")
  @PreAuthorize("hasPermission(#messageId, 'Message', 'WRITE')")
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable String messageId,
      @Valid @RequestBody UpdateMessageDto updateMessageDto) {
    log.info("메세지 수정 요청: messageId = {}", messageId);
    try {
      MessageDto messageDto = messageService.updateMessage(messageId, updateMessageDto);
      return ResponseEntity.ok(messageDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  /*  //특정 채널의 최근 50개 메세지 조회
    @GetMapping
    public ResponseEntity<List<MessageDto>> getAllMessages(@RequestParam String channelId,
        @RequestParam Pageable pageable) {
      PageResponse<MessageDto> allByChannelIdWithPaging = messageService.findAllByChannelIdWithPaging(
          channelId, pageable);

      return ResponseEntity.ok().body(allByChannelIdWithPaging.getContents());
    }*/

  //이것도 요청한 유저가 해당 채널(특히 PRIVATE)에 속하는지 검증해야하지 않을까?
  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> getMessagesWithCursor(
      @RequestParam String channelId,
      @RequestParam(required = false) Instant cursor,
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    PageResponse<MessageDto> response = messageService.findAllByChannelIdWithCursor(
        channelId, cursor, pageable);

    return ResponseEntity.ok().body(response);
  }

  //메세지 삭제
  @PreAuthorize("hasPermission(#messageId, 'Message', 'DELETE')")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<String> deleteMessage(@PathVariable String messageId,
      @RequestParam String userId) {
    log.info("메세지 삭제 요청: messageId = {}", messageId);
    try {
      messageService.delete(messageId, userId);
      return ResponseEntity.ok().body("Message (id=" + messageId + ") deleted");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}

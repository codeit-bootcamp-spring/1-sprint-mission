package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.MessageService;
import io.micrometer.core.annotation.Timed;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
  private final SimpMessagingTemplate messagingTemplate;

  //특정 채널 메세지 생성 - 첨부파일 포함
  @Timed("message.create.async")
  @PostMapping
  public ResponseEntity<?> createMessage(
      @RequestPart(value = "attachments") List<MultipartFile> attachments,
      @Valid @RequestPart(value = "messageCreateRequest") MessageCreateRequest messageCreateRequest) {

    log.info("메세지 생성 요청: {}", messageCreateRequest);

    if (attachments != null && !attachments.isEmpty()) {
      return ResponseEntity.badRequest().body(ErrorCode.EMPTY_DATA);
    }

    try {
      MessageDto messageDto = messageService.create(messageCreateRequest, attachments);

      messagingTemplate.convertAndSend(
          "/sub/channels." + messageCreateRequest.getChannelId() + ".messages",
          messageDto
      );

      return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }

  }

  //특정 채널 메세지 수정
  //@PreAuthorize("hasRole('ADMIN') or #updateMessageDto.userId() == authentication.principal.user.id")
  @PreAuthorize("principal.userDto.id == @basicMessageService.find(#messageId).author.id")
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
  @PreAuthorize("principal.userDto.id == @basicMessageService.find(#messageId).author.id or hasRole('ADMIN')")
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

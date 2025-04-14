package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping(value = "", consumes = {
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    log.info("메시지 생성 요청: request={}, attachments={}", messageCreateRequest, attachments);
    MessageDto createMessage = messageService.create(messageCreateRequest, attachments);
    log.debug("메시지 생성 응답: {}", createMessage);
    return ResponseEntity.status(HttpStatus.CREATED).body(createMessage);
  }

  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest request
  ) {
    log.info("메시지 수정 요청: id={}, request={}", messageId, request);
    MessageDto updateMessage = messageService.update(messageId, request);
    log.debug("메시지 수정 응답: {}", updateMessage);
    return ResponseEntity.status(HttpStatus.OK).body(updateMessage);
  }

  @DeleteMapping(value = "/{messageId}/{writerId}")
  public ResponseEntity<Void> deleteMessage(
      @PathVariable("messageId") UUID messageId,
      @PathVariable("writerId") UUID writerId) {
    log.info("메시지 삭제 요청: id={}", messageId);
    messageService.delete(messageId, writerId);
    log.debug("메시지 삭제 완료");
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> getChannelMessages(
      @RequestParam("channelId") UUID channelId,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      @PageableDefault(
          size = 50,
          page = 0,
          sort = "createdAt",
          direction = Direction.DESC
      ) Pageable pageable) {
    log.info("채널별 메시지 목록 조회 요청: channelId={}, cursor={}, pageable={}",
        channelId, cursor, pageable);
    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
        pageable);
    log.debug("채널별 메시지 목록 조회 응답: totalElements={}", messages.totalElements());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }

}

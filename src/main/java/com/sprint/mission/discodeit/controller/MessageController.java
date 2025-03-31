package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.basic.MessageService;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/messages")
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    log.info("메시지 생성 요청 수신 - channelId: {}, authorId: {}, 첨부파일 수: {}",
        messageCreateRequest.channelId(),
        messageCreateRequest.authorId(),
        attachments != null ? attachments.size() : 0);
    try {
      List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
          .map(files -> files.stream()
              .map(file -> {
                try {
                  return new BinaryContentCreateRequest(
                      file.getOriginalFilename(),
                      file.getContentType(),
                      file.getBytes()
                  );
                } catch (IOException e) {
                  log.error("첨부파일 처리 중 IOException - fileName: {}, 원인: {}",
                      file.getOriginalFilename(), e.getMessage(), e);
                  throw new RuntimeException(e);
                }
              })
              .toList())
          .orElse(new ArrayList<>());
      MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
      log.info("메시지 생성 성공 - messageId: {}, channelId: {}", createdMessage.id(),
          createdMessage.channelId());
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(createdMessage);
    } catch (Exception e) {
      log.error("메시지 생성 실패 - channelId: {}, authorId: {}, 원인: {}",
          messageCreateRequest.channelId(), messageCreateRequest.authorId(), e.getMessage(), e);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  @PatchMapping(path = "{messageId}")
  public ResponseEntity<MessageDto> update(@PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    log.info("메시지 업데이트 요청 수신 - messageId: {}", messageId);
    try {
      MessageDto updatedMessage = messageService.update(messageId, request);
      log.info("메시지 업데이트 성공 - messageId: {}", messageId);
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(updatedMessage);
    } catch (Exception e) {
      log.error("메시지 업데이트 실패 - messageId: {}, 원인: {}", messageId, e.getMessage(), e);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }

  }

  @DeleteMapping(path = "{messageId}")
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
    log.info("메시지 삭제 요청 수신 - messageId: {}", messageId);
    try {
      messageService.delete(messageId);
      log.info("메시지 삭제 성공 - messageId: {}", messageId);
      return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .build();
    } catch (Exception e) {
      log.error("메시지 삭제 실패 - messageId: {}, 원인: {}", messageId, e.getMessage(), e);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      @PageableDefault(
          size = 50,
          page = 0,
          sort = "createdAt",
          direction = Direction.DESC
      ) Pageable pageable) {
    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
        pageable);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}

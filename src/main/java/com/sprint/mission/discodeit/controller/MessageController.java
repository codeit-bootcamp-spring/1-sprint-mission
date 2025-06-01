package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
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

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
      @Valid @RequestPart(value = "MessageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    log.info("메시지 생성 요청 : request={}, attachmentCount={}", request, attachments.size());
    List<BinaryContentCreateRequest> attachmentRequests = BinaryContentUtil.convertToBinaryContentRequests(
        attachments);
    MessageDto messageDto = messageService.create(request, attachmentRequests);
    log.info("메시지 생성 응답 : {}", messageDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(messageDto);
  }

  @Override
  @PatchMapping(path = "{messageId}")
  public ResponseEntity<MessageDto> update(
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest request) {

    log.info("Message 수정 요청 : id={}, request={}", messageId, request);
    MessageDto messageDto = messageService.update(messageId, request);
    log.info("Message 수정 응답 : {}", messageDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messageDto);
  }

  @Override
  @DeleteMapping(path = "{messageId}")
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {

    log.info("메시지 삭제 요청 : id={}", messageId);
    messageService.delete(messageId);
    log.info("Message 삭제 완료");

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
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

    log.info("채널별 메시지 목록 조회 요청: channelId={}, cursor={}, pageable={}", channelId, cursor, pageable);
    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
        pageable);
    log.debug("채널별 메시지 목록 조회 응답: totalElements={}", messages.totalElements());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}

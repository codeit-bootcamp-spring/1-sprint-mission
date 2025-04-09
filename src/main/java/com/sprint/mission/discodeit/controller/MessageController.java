package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.BinaryContentStoreDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.IOException;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<MessageDto> createMessage(
      @ModelAttribute MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentStoreDto> attachmentRequest = BinaryContentMapper.toDtoFromMultipartFile(attachments);
    MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    messageService.update(messageId, request.getRequesterId(), request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .build();
  }


  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId,
      @RequestParam("requesterId") UUID requesterId) {
    messageService.delete(messageId, requesterId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  // 페이징 구현
  @GetMapping("/{channelId}")
  public ResponseEntity<PageResponse<MessageDto>> getMessages(
      @PathVariable UUID channelId,
      @RequestParam(required = false) String cursor,
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
      @RequestParam
      Pageable pageable) {

    Page<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor, pageable);

    String nextCursor = getNextCursor(messages, cursor);

    PageResponse<MessageDto> messageDtoPageResponse = PageResponseMapper.fromPage(messages, nextCursor);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messageDtoPageResponse);
  }

  // cursor을 Base64 문자열로 인코딩
  private String encodeCursor(MessageDto messages, String cursor) {
    if (messages == null || messages.getId() == null) {
      throw new IllegalArgumentException("Invalid cursor data");
    }
    return Base64.getEncoder().encodeToString((messages.getId().toString()).getBytes());
  }

  // nextCursor 생성
  private String getNextCursor(Page<MessageDto> messages, String cursor){
    List<MessageDto> content = messages.getContent();
    String nextCursor = messages.hasNext() && !content.isEmpty() ?
            encodeCursor(content.get(content.size() - 1), cursor) : null;
    return nextCursor;
  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import io.netty.handler.codec.http2.Http2FrameLogger;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageApi {

  private final MessageService messageService;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart("messageCreateRequest") CreateMessageRequestDto messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    List<BinaryContentDto> attachmentRequests = attachments != null
        ? attachments.stream()
        .map(this::saveAttachment)
        .collect(Collectors.toList())
        : List.of();

    MessageDto createdMessage = messageService.createMessage(messageCreateRequest,
        attachmentRequests);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
  }


  @Override
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable("messageId") UUID messageId,
      @RequestBody UpdateMessageRequestDto request) {
    MessageDto updatedMessage = messageService.updateMessage(messageId, request);
    return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
  }

  @Override
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.deleteMessage(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


  @Override
  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      @RequestParam(value = "size", defaultValue = "50") int size) {
    PageResponse<MessageDto> response = messageService.findAllByChannelId(channelId, cursor, size);
    return ResponseEntity.ok(response);
  }

  private BinaryContentDto saveAttachment(MultipartFile multipartFile) {
    return messageService.saveAttachment(multipartFile);
  }
}

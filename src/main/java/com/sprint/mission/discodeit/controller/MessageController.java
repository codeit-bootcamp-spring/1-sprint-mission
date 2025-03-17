package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.MessageSwagger;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.util.FileConverter;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageSwagger {

  private final BasicMessageService messageService;

  private final MessageMapper messageMapper;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> attachmentRequests = FileConverter.getAttachmentRequests(
        attachments);
    Message savedMessage = messageService.create(request, attachmentRequests);

    Message message = messageService.findWithAllRelationships(savedMessage.getId());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(messageMapper.toDto(message));
  }


  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageDto> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request
  ) {
    Message message = messageService.update(messageId, request);
    return ResponseEntity.ok(messageMapper.toDto(message));
  }

  @DeleteMapping(value = "/{messageId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID messageId
  ) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  //  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findMessagesByChannel(
      @RequestParam UUID channelId,
      Pageable pageable
  ) {
    PageResponse<MessageDto> messageDtoPageResponse = messageService.findMessageDtosByChannelId(
        channelId, pageable);
    return ResponseEntity.ok(messageDtoPageResponse);
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findMessagesByChannel(
      @RequestParam UUID channelId,
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant cursor,
      Pageable pageable
  ) {
    // 커서 기반 페이지네이션 메서드 호출
    PageResponse<MessageDto> messageDtoPageResponse =
        messageService.findMessageDtosByChannelIdWithCursor(channelId, cursor, pageable);
    return ResponseEntity.ok(messageDtoPageResponse);
  }

}
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageControllerDocs {

  private final MessageService messageService;

  @PostMapping
  @Override
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart @Valid MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments
  ) {
    MessageDto messageDto = messageService.createMessage(messageCreateRequest,
        attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
  }

  @GetMapping
  @Override
  public ResponseEntity<PageResponse<MessageDto>> getMessages(
      @RequestParam UUID channelId,
      @RequestParam(required = false) Instant cursor,
      Pageable pageable
  ) {
    return ResponseEntity
        .ok(messageService.readAllByChannelId(channelId, cursor, pageable));
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<MessageDto> updateMessage(
      @PathVariable UUID id,
      @RequestBody @Valid MessageUpdateRequest messageUpdateRequest
  ) {
    MessageDto messageDto = messageService.updateMessage(id,
        messageUpdateRequest.newContent());
    return ResponseEntity.ok(messageDto);
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> deleteMessage(
      @PathVariable UUID id
  ) {
    messageService.deleteMessage(id);
    return ResponseEntity.noContent().build();
  }
}

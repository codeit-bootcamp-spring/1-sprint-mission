package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageControllerDocs {

  private final MessageService messageService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<MessageResponseDto> createMessage(
      @Valid @RequestPart("messageCreateRequest") MessageRequestDto messageReqDto,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws IOException {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(messageService.create(messageReqDto, attachments));
  }

  @PreAuthorize("@basicMessageService.isAuthor(#messageId, authentication.principal.id)")
  @PutMapping(path = "/{messageId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
      MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable UUID messageId,
      @RequestPart("content") String content,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws IOException {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(messageService.update(messageId, content, attachments));
  }

  @PreAuthorize("@basicMessageService.isAuthor(#messageId, authentication.principal.id) or hasRole('ADMIN')")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<MessageResponseDto> deleteMessage(@PathVariable UUID messageId) {
    return ResponseEntity.status(HttpStatus.CREATED).body(messageService.delete(messageId));
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageResponseDto>> getMessagesByChannel(
      @RequestParam UUID channelId,
      @RequestParam(required = false) UUID cursor
  ) {
    return ResponseEntity.ok(messageService.findMessagesByChannelId(channelId, cursor));
  }
}

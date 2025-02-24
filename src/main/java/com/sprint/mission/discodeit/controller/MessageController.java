package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Message> create(
      @RequestPart("messageCreate") MessageCreateDto messageCreateDto,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateDto> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(this::processAttachment).collect(Collectors.toList()))
        .orElse(List.of());
    Message createdMessage = messageService.createMessage(messageCreateDto, attachmentRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  @PutMapping("/{messageId}")
  public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateDto messageUpdateDto) {
    Message updatedMessage = messageService.update(messageId, messageUpdateDto);
    return ResponseEntity.ok(updatedMessage);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/channel/{channelId}")
  public ResponseEntity<List<Message>> findAllByChannelId(@PathVariable UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messages);
  }

  private BinaryContentCreateDto processAttachment(MultipartFile file) {
    try {
      return new BinaryContentCreateDto(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      );
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 파일입니다", e);
    }
  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  // 메시지 전송
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
    List<BinaryContentCreateRequest> attachmentsRequest = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes());
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }).toList()).orElse(new ArrayList<>());
    MessageDto messageDto = messageService.create(messageCreateRequest, attachmentsRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
  }

  // 메시지 수정
  @PutMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    MessageDto messageDto = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity.ok(messageDto);
  }

  // 메시지 삭제
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  // 특정 채널의 메시지 조회
  @GetMapping("/channels/{channelId}")
  public ResponseEntity<List<MessageDto>> getMessagesOfChannel(@PathVariable UUID channelId) {
    List<MessageDto> messageDtoList = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messageDtoList);
  }
}

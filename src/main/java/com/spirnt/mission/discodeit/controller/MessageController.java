package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.MessageApiDocs;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.response.PageResponse;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApiDocs {

  private final MessageService messageService;

  // 메시지 전송
  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> createMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments) {
    MessageDto message = messageService.create(messageCreateRequest, attachments);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(message);
  }

  // 메시지 수정
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest) {
    MessageDto message = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity.ok(message);
  }

  // 메시지 삭제
  @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .build();
  }

  // 특정 채널의 메시지 목록 조회
  @GetMapping("")
  public ResponseEntity<PageResponse<Message>> getAllMessagesByChannel(@RequestParam UUID channelId,
      @RequestParam(required = false) Instant cursor,
      @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
      Pageable pageable) {
    PageResponse<Message> messages = messageService.findAllByChannelId(channelId, cursor, pageable);
    return ResponseEntity.ok(messages);
  }
}

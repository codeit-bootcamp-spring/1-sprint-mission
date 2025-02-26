package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Message", description = "메세지 API")
public class MessageController {

  private final MessageService messageService;
  private final ChannelService channelService;

  @GetMapping("/all")
  public List<MessageResponseDto> getAllMessages() {
    return messageService.findAll();
  }

  //특정 채널 메세지 생성
  @PostMapping
  public ResponseEntity<MessageResponseDto> createMessage(
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments,
      @RequestPart(value = "messageCreateRequest") CreateMessageDto createMessageDto) {

    MessageResponseDto messageDto;

    if (attachments != null && !attachments.isEmpty()) {
      messageDto = messageService.create(createMessageDto, attachments);
    } else {
      messageDto = messageService.create(createMessageDto);
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
  }

  //특정 채널 메세지 수정
  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageResponseDto> updateMessage(@PathVariable String messageId,
      @RequestBody UpdateMessageDto updateMessageDto) {
    return ResponseEntity.ok(messageService.updateMessage(messageId, updateMessageDto));
  }

  //특정 사용자의 모든 메세지 목록 조회
  //todo - 고민: UserController로 옮기는게 나을까?
  @GetMapping("/users")
  public ResponseEntity<List<MessageResponseDto>> getMessagesByUserId(@RequestParam String userId) {
    //@RequestHeader(value = "If-None-Match") String ifNoneMatch) {
    List<MessageResponseDto> allBySenderId = messageService.findAllBySenderId(userId);

    String etag = "\"" + allBySenderId + "\"";

//    if (etag.equals(ifNoneMatch)) {
//      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
//    }
//
//    return ResponseEntity.ok().eTag(etag).cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
//        .body(allBySenderId);
    return ResponseEntity.ok().body(allBySenderId);
  }


  //특정 채널의 모든 메세지 조회
  @GetMapping
  public ResponseEntity<List<MessageResponseDto>> getAllMessages(@RequestParam String channelId) {
    //   @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
    List<MessageResponseDto> allMessages = channelService.findAllMessagesByChannelId(channelId);
    String etag = "\"" + allMessages.hashCode() + "\"";
//    if (etag.equals(ifNoneMatch)) {
//      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
//    }
//    return ResponseEntity.ok().eTag(etag).cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
//        .body(allMessages);

    return ResponseEntity.ok().body(allMessages);
  }

  //메세지 삭제
  @DeleteMapping("/{messageId}")
  public ResponseEntity<String> deleteMessage(@PathVariable String messageId,
      @RequestParam String userId) {
    if (messageService.delete(messageId, userId)) {
      return ResponseEntity.ok().body("Message (id=" + messageId + ") deleted");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }
}

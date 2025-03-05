package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.message.MessageAndFileCreateRequest;
import com.sprint.mission.discodeit.entity.message.MessageAndFileCreateResponse;
import com.sprint.mission.discodeit.entity.message.MessageContentUpdateRequest;
import com.sprint.mission.discodeit.entity.message.MessageUpdateResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message Controller")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageControllerV2 {

  private final MessageService messageService;

  /**
   * 메세지 생성
   */
  @Operation(summary = "메세지 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public MessageAndFileCreateResponse sendMessage(
      @RequestPart("messageCreateRequest") MessageAndFileCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    return messageService.create(messageCreateRequest, attachments);
  }

  /**
   * 채널의 메세지 목록 조회
   */
  @Operation(summary = "채널 메세지 목록 조회")
  @GetMapping
  public List<MessageAndFileCreateResponse> findAll(@RequestParam("channelId") UUID channelId) {
    return messageService.findAllMessage(channelId);
  }

  /**
   * 메세지 삭제
   */
  @Operation(summary = "메세지 삭제")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{messageId}")
  public String deleteMessage(@PathVariable("messageId") UUID messageId) {
    messageService.remove(messageId);

    return "삭제 완료";
  }

  /**
   * 메세지 내용 수정
   */
  @Operation(summary = "메세지 수정")
  @PatchMapping("/{messageId}")
  public MessageUpdateResponse updateMessage(@PathVariable("messageId") UUID messageId,
      @RequestBody MessageContentUpdateRequest request) {
    return messageService.updateMessage(messageId, request);
  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.message.MessageContentUpdateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
public class MessageController {

  private final MessageService messageService;

  /**
   * 메세지 생성
   */
  @Operation(summary = "메세지 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public MessageDto sendMessage(
      @RequestPart("messageCreateRequest") MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    return messageService.create(request, attachments);
  }

  /**
   * 채널의 메세지 목록 조회
   * <p>
   * 커서 기반 페이징
   */
  @Operation(summary = "채널 메세지 목록 조회")
  @GetMapping
  public PageResponse<MessageDto> findAll(@RequestParam(name = "channelId") UUID channelId,
      @RequestParam(name = "cursor", required = false) Instant cursor,
      Pageable pageable) {

    return messageService.findMessagesWithPaging(channelId, cursor, pageable);
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
  public MessageDto updateMessage(@PathVariable("messageId") UUID messageId,
      @RequestBody @Validated MessageContentUpdateRequest request) {

    return messageService.update(messageId, request);
  }
}

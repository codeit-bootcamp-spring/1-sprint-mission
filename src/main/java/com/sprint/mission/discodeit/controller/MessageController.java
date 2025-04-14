package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
      @Valid @RequestPart(value = "CreateMessageRequest") CreateMessageRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

    log.info("Message 생성 요청 : {}", request);

    List<CreateBinaryContentRequest> attachmentRequests = BinaryContentUtil.convertToBinaryContentRequests(
        attachments);
    MessageDto messageDto = messageService.create(request, attachmentRequests);

    log.debug("Message 생성 응답 : {}", messageDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(messageDto);
  }

  @Override
  @PatchMapping(path = "{messageId}")
  public ResponseEntity<MessageDto> update(
      @PathVariable("messageId") UUID messageId,
      @RequestBody UpdateMessageRequest request) {

    log.info("Message 수정 요청 : messageId={}, request={}", messageId, request);

    MessageDto messageDto = messageService.update(messageId, request);

    log.debug("Message 수정 응답 : {}", messageDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messageDto);
  }

  @Override
  @DeleteMapping(path = "{messageId}")
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {

    log.info("Message 삭제 요청 : messageId={}", messageId);

    messageService.delete(messageId);

    log.debug("Message 삭제 성공 : messageId={}", messageId);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      @PageableDefault(
          size = 50,
          page = 0,
          sort = "createAt",
          direction = Direction.DESC
      ) Pageable pageable) {

    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
        pageable);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}

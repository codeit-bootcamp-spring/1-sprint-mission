package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;


  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<MessageDto> createMessage(
      @ModelAttribute MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
      // MultipartFile : 스프링이 제공하는 인터페이스로 파일 업로드할 때 사용하는 객체, 아래 관련 메서드 사용함
  ) {
    List<BinaryContentDto> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              return new BinaryContentDto(
                  UUID.randomUUID(),
                  file.getOriginalFilename(),
                  (int) file.getSize(), //getSize() -> long으로 반환
                  file.getContentType()
              );
            })
            .toList())
        .orElse(new ArrayList<>());
    MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest request) {
    messageService.update(messageId, request.getRequesterId(), request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .build();
  }


  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId,
      @RequestParam("requesterId") UUID requesterId) {
    messageService.delete(messageId, requesterId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  // 페이징 구현
  @GetMapping("/{channelId}")
  public ResponseEntity<PageResponse<MessageDto>> getMessages(
      @PathVariable UUID channelId,
      @PageableDefault(page=0, size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
      @RequestParam
      Pageable pageable) {
    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, pageable);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}

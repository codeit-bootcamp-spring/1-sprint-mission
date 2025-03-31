package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<MessageDto> createMessage(
      @Valid @RequestPart(value = "messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "binaryContents", required = false) List<MultipartFile> attachments)
      throws Exception {
    log.info("메세지 생성 요청(Request): messageContent={}, hasProfileImage={}",
        messageCreateRequest.content(),
        attachments != null);

    // 메세지 첨부 파일 생성
    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
    if (attachments != null) {
      for (MultipartFile file : attachments) {
        log.debug("메세지 첨부 파일 생성: fileName={}", file.getName());
        binaryContentCreateRequests.add(new BinaryContentCreateRequest(file));
      }
    }

    // 메세지 생성
    MessageDto messageDto = messageService.createMessage(messageCreateRequest,
        binaryContentCreateRequests);

    log.info("메세지 생성 응답(Response): messageContent={}, HttpStatus={}",
        messageDto.content(),
        HttpStatus.OK);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(messageDto);
  }

  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
      @Valid @RequestBody MessageUpdateRequest messageUpdateRequest) {
    log.info("메세지 수정 요청(Request): messageChanged={}", !messageUpdateRequest.newMessage().isEmpty());

    // 메세지 수정
    MessageDto messageDto = messageService.updateMessageText(messageId, messageUpdateRequest);
    log.info("메세지 수정 응답(Response): newContent={}, HttpStatus={}",
        messageDto.content(),
        HttpStatus.OK);
    return ResponseEntity.ok(messageDto);
  }

  @DeleteMapping(value = "/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    log.info("메세지 삭제 요청(Request)");
    // 메세지 삭제
    messageService.deleteMessageById(messageId);
    log.info("메세지 삭제 응답(Response): HttpStatus={}", HttpStatus.NO_CONTENT);
    return ResponseEntity.noContent().build(); // 204
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> getMessageByChannelId(
      @RequestParam UUID channelId,
      @RequestParam(defaultValue = "0") int page, // 0페이지 부터
      @RequestParam(defaultValue = "50") int size, // 50개씩
      @RequestParam(defaultValue = "createDate") String sortBy, // 정렬 기준
      @RequestParam(defaultValue = "desc") String direction) { // 내림차순 --> 최신 것부터
    // PageRequest.of(page, size, sort)
    // 0, 10, Sort.by("orderDate").descending()

    // Sort 객체 생성
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    // Pageable 객체 생성
    Pageable pageable = PageRequest.of(page, size, sort);

    // Service 호출
    PageResponse<MessageDto> messagePageResponse = messageService.findAllByChannelId(channelId,
        pageable);

    return ResponseEntity.ok().body(messagePageResponse); // 200
  }
}

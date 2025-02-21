package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//네이밍, HTTP 메서드, 상태 코드, 응답 구조 일관성
@RestController
@RequestMapping("/api/messages")
@Validated
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  //메시지 생성
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<MessageResponseDTO> createMessage(
      @RequestPart("message-create-dto") @Valid MessageCreateDTO messageCreateDTO,
      @RequestPart("binary-content-create-request") MultipartFile binaryContentCreateRequest
  ) {
    BinaryContentCreateRequest binaryContent = null;
    if (Objects.nonNull(binaryContentCreateRequest)) {
      try {
        binaryContent = new BinaryContentCreateRequest(
            binaryContentCreateRequest.getOriginalFilename(),
            binaryContentCreateRequest.getContentType(),
            binaryContentCreateRequest.getBytes()
        );
      } catch (IOException exception) {
        throw new IllegalArgumentException(exception);
      }
    }
    MessageResponseDTO messageResponseDTO = messageService.create(messageCreateDTO, binaryContent);
    return ResponseEntity.status(HttpStatus.CREATED).body(messageResponseDTO);
  }

  //메세지 수정
  @PatchMapping("/modify")
  public ResponseEntity<MessageResponseDTO> modifyMessage(
      @RequestBody @Valid MessageUpdateDTO messageUpdateDTO) {
    MessageResponseDTO update = messageService.update(messageUpdateDTO);
    return ResponseEntity.ok(update);
  }

  //메세지 삭제
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  //특정채널의 메시지 목록 조회
  @GetMapping("/{channelId}")
  public ResponseEntity<List<MessageResponseDTO>> getListMessages(@PathVariable UUID channelId) {
    List<MessageResponseDTO> messagesByChannelId = messageService.findMessagesByChannelId(
        channelId);
    return ResponseEntity.ok(messagesByChannelId);
  }

}

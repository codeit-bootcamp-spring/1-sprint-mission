package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "메세지 관련 정보")
public class MessageController {

  private final MessageService messageService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "메시지 생성",
      description = "첨부 파일을 선택적으로 포함하여 새 메시지를 생성합니다.",
      responses = {
          @ApiResponse(
              responseCode = "201",
              description = "메시지 생성 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "400",
              description = "잘못된 메시지 데이터",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Invalid message data format"
                  )
              )
          )
      }
  )

  public ResponseEntity<MessageResponse> create(
      @Parameter(description = "메시지 생성 정보")
      @RequestPart("messageCreateRequest") String messageCreateRequestStr,
      @Parameter(description = "선택적 메시지 첨부 파일")
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    try {
      MessageCreateRequest messageCreateRequest = objectMapper.readValue(messageCreateRequestStr,
          MessageCreateRequest.class);

      List<BinaryContentCreateRequest> attachmentRequests = new ArrayList<>();
      if (attachments != null) {
        for (MultipartFile file : attachments) {
          BinaryContentCreateRequest request = new BinaryContentCreateRequest(
              file.getOriginalFilename(),
              file.getContentType(),
              file.getBytes()
          );
          attachmentRequests.add(request);
        }
      }

      Message createdMessage = messageService.create(
          messageCreateRequest.authorId(),
          messageCreateRequest.channelId(),
          messageCreateRequest.content(),
          attachmentRequests
      );

      MessageResponse response = MessageResponse.from(createdMessage);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @GetMapping
  @Operation(
      summary = "채널별 메시지 조회",
      description = "특정 채널의 모든 메시지를 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "메시지 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResponse.class)
              )
          )
      }
  )
  public ResponseEntity<List<MessageResponse>> findAllByChannelId(
      @Parameter(description = "채널 ID로 채널별 메세지들 조회")
      @RequestParam("channelId") UUID channelId) {
    List<Message> messages = messageService.getMessagesByChannel(channelId);
    List<MessageResponse> responses = new ArrayList<>();
    for (Message message : messages) {
      responses.add(MessageResponse.from(message));
    }
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responses);
  }

  @GetMapping("/author/{authorId}")
  @Operation(
      summary = "작성자별 메시지 조회",
      description = "특정 작성자가 생성한 모든 메시지를 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "메시지 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "메시지를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Message with id {messageId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<List<MessageResponse>> getMessagesByAuthorId(
      @Parameter(description = "작성자 ID로 조회")
      @PathVariable("authorId") UUID authorId) {
    List<Message> messages = messageService.getMessagesByAuthor(authorId);
    if (messages.isEmpty()) {
      throw new NoSuchElementException("No messages found for authorId: " + authorId);
    }

    List<MessageResponse> responses = new ArrayList<>();
    for (Message message : messages) {
      responses.add(MessageResponse.from(message));
    }

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  @GetMapping("/{messageId}")
  @Operation(
      summary = "메시지 ID로 조회",
      description = "특정 ID의 메시지를 조회합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "메시지 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "메시지를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Message with id {messageId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<MessageResponse> getMessageById(
      @Parameter(description = "메세지 ID로 조회")
      @PathVariable("messageId") UUID messageId) {
    Message message = messageService.getMessageById(messageId);
    if (message == null) {
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }
    MessageResponse response = MessageResponse.from(message);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PatchMapping("/{messageId}")
  @Operation(
      summary = "메시지 내용 수정",
      description = "특정 메시지의 내용을 수정합니다.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "메시지 수정 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageResponse.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "메시지를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Message with id {messageId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<MessageResponse> update(
      @Parameter(description = "메세지 ID로 수정")
      @PathVariable("messageId") UUID messageId,
      @RequestBody MessageUpdateRequest request
  ) {
    Message updatedMessage = messageService.updateMessageContent(messageId,
        request.newContent());
    if (updatedMessage == null) {
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }
    MessageResponse response = MessageResponse.from(updatedMessage);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(response);
  }

  @DeleteMapping("/{messageId}")
  @Operation(
      summary = "메시지 삭제",
      description = "특정 메시지를 삭제합니다.",
      responses = {
          @ApiResponse(
              responseCode = "204",
              description = "메시지 삭제 성공"
          ),
          @ApiResponse(
              responseCode = "404",
              description = "메시지를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  examples = @ExampleObject(
                      value = "Message with id {messageId} not found"
                  )
              )
          )
      }
  )
  public ResponseEntity<Void> delete(
      @Parameter(description = "메세지 ID로 삭제")
      @PathVariable("messageId") UUID messageId) {
    boolean deleted = messageService.deleteMessage(messageId);
    if (deleted) {
      return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .build();
    } else {
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }
  }
}
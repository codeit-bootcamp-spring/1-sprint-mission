package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "메시지 관리", description = "메시지 관련 API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "메시지 생성", description = "새로운 메시지를 생성합니다.")
  @ApiResponse(responseCode = "201", description = "메시지 생성 성공")
  @PostMapping
  public ResponseEntity<MessageResponse> createMessage(@RequestBody CreateMessageRequest request) {
    MessageResponse response = messageService.createMessage(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "전체 메시지 조회", description = "저장된 모든 메시지를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "메시지 목록 조회 성공")
  @GetMapping
  public ResponseEntity<List<MessageResponse>> getAllMessages() {
    return ResponseEntity.ok(messageService.getMessages());
  }

  @Operation(summary = "전체 메시지 페이징 조회", description = "저장된 모든 메시지를 페이징으로 조회합니다.")
  @ApiResponse(responseCode = "200", description = "메시지 목록 조회 성공")
  @GetMapping("/page")
  public ResponseEntity<PageResponse<MessageResponse>> getAllPagingMessages(
      @RequestParam int page, @RequestParam int size
  ) {
    return ResponseEntity.ok(messageService.getPageMessages(page, size));
  }

  @Operation(summary = "특정 메시지 조회", description = "메시지 ID를 이용하여 특정 메시지를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메시지 조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 ID의 메시지를 찾을 수 없음")
  })
  @GetMapping("/{id}")
  public ResponseEntity<MessageResponse> getMessageById(
      @Parameter(description = "조회할 메시지의 ID", required = true) @PathVariable UUID id) {
    return ResponseEntity.ok(messageService.getMessage(id));
  }

  @Operation(summary = "채널별 메시지 조회", description = "특정 채널의 모든 메시지를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "채널별 메시지 조회 성공")
  @GetMapping("/channel/{channelId}")
  public ResponseEntity<List<MessageResponse>> getMessagesByChannel(
      @Parameter(description = "메시지를 조회할 채널의 ID", required = true) @PathVariable UUID channelId) {
    return ResponseEntity.ok(messageService.getMessagesByChannel(channelId));
  }

  @Operation(summary = "메시지 수정", description = "메시지를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메시지 수정 성공"),
      @ApiResponse(responseCode = "404", description = "해당 메시지를 찾을 수 없음")
  })
  @PatchMapping("{id}")
  public ResponseEntity<MessageResponse> updateMessage(
      @Parameter(description = "업데이트할 메시지의 ID", required = true) @PathVariable UUID id,
      @RequestBody UpdateMessageRequest request) {
    return ResponseEntity.ok(messageService.updateMessage(id, request));
  }

  @Operation(summary = "메시지 삭제", description = "메시지 ID를 이용하여 메시지를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "메시지 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 메시지를 찾을 수 없음")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMessage(
      @Parameter(description = "삭제할 메시지의 ID", required = true) @PathVariable UUID id) {
    messageService.deleteMessage(id);
    return ResponseEntity.noContent().build();
  }
}

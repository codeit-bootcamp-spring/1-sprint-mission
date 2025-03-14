package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.CursorResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag(name = "메시지 관리", description = "메시지 관련 API")
@RequestMapping("/api/messages")
public interface MessageApi {

  @Operation(summary = "메시지 생성", description = "새로운 메시지를 생성합니다.")
  @ApiResponse(responseCode = "201", description = "메시지 생성 성공")
  @PostMapping
  ResponseEntity<MessageResponse> createMessage(@RequestBody CreateMessageRequest request);

  @Operation(summary = "전체 메시지 조회", description = "저장된 모든 메시지를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "메시지 목록 조회 성공")
  @GetMapping
  ResponseEntity<List<MessageResponse>> getAllMessages();

  @Operation(summary = "전체 메시지 페이징 조회", description = "저장된 모든 메시지를 페이징으로 조회합니다.")
  @ApiResponse(responseCode = "200", description = "메시지 목록 조회 성공")
  @GetMapping("/page")
  ResponseEntity<PageResponse<MessageResponse>> getAllPagingMessages(
      @RequestParam int page, @RequestParam int size
  );

  @Operation(summary = "전체 메시지 커서 조회", description = "저장된 모든 메시지를 커서 페이징으로 조회합니다.")
  @ApiResponse(responseCode = "200", description = "메시지 목록 조회 성공")
  @GetMapping("/cursor")
  ResponseEntity<CursorResponse<Message>> getAllCursorMessages(
      @RequestParam Instant cursor, @RequestParam int size
  );

  @Operation(summary = "특정 메시지 조회", description = "메시지 ID를 이용하여 특정 메시지를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메시지 조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 ID의 메시지를 찾을 수 없음")
  })
  @GetMapping("/{id}")
  ResponseEntity<MessageResponse> getMessageById(
      @Parameter(description = "조회할 메시지의 ID", required = true) @PathVariable UUID id
  );

  @Operation(summary = "채널별 메시지 조회", description = "특정 채널의 모든 메시지를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "채널별 메시지 조회 성공")
  @GetMapping("/channel/{channelId}")
  ResponseEntity<List<MessageResponse>> getMessagesByChannel(
      @Parameter(description = "메시지를 조회할 채널의 ID", required = true) @PathVariable UUID channelId
  );

  @Operation(summary = "메시지 수정", description = "메시지를 수정합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "메시지 수정 성공"),
      @ApiResponse(responseCode = "404", description = "해당 메시지를 찾을 수 없음")
  })
  @PatchMapping("{id}")
  ResponseEntity<MessageResponse> updateMessage(
      @Parameter(description = "업데이트할 메시지의 ID", required = true) @PathVariable UUID id,
      @RequestBody UpdateMessageRequest request
  );

  @Operation(summary = "메시지 삭제", description = "메시지 ID를 이용하여 메시지를 삭제합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "메시지 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "해당 메시지를 찾을 수 없음")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteMessage(
      @Parameter(description = "삭제할 메시지의 ID", required = true) @PathVariable UUID id
  );
}

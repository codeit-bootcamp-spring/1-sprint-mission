package com.sprint.mission.discodeit.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageApi {

  @Operation(summary = "Message 생성", description = "새로운 메시지를 생성합니다.")
  ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") String jsonRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws JsonProcessingException;

  @Operation(
      summary = "Message 내용 수정",
      description = "특정 메시지의 내용을 업데이트합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 수정 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<MessageDto> update(@Parameter(description = "수정할 메시지 ID", required = true)
      @PathVariable("id") UUID id,
      @RequestBody MessageUpdateRequest request);

  @Operation(summary = "Message 삭제", description = "메시지를 삭제합니다.")
  ResponseEntity<Void> delete(@PathVariable("id") UUID id);

  @Operation(summary = "Channel의 Message 목록 조회", description = "특정 채널의 메시지를 조회합니다.")
  ResponseEntity<List<MessageDto>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId);
}

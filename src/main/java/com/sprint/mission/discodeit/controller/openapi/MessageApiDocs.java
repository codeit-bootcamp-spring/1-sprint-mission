package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Message API", description = "Message 작업")
public interface MessageApiDocs {

  @Operation(summary = "Message 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Message 생성 성공"),
      @ApiResponse(
          responseCode = "404",
          description = "User 혹은 Channel 을 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          ))
  })
  ResponseEntity<MessageResponseDto> sendMessage(
      @Parameter(description = "메시지 생성 정보", required = true) CreateMessageDto messageCreateRequest,
      @Parameter(
          description = "바이너리 첨부 파일",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(type = "string", format = "binary")
          ),
          required = false
      ) List<MultipartFile> files);

  @Operation(summary = "Message 업데이트")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 업데이트 성공"),
      @ApiResponse(
          responseCode = "404",
          description = "Message 를 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<MessageResponseDto> updateMessage(
      @Parameter(required = true, description = "업데이트 Message UUID") String messageId,
      @Parameter(required = true, description = "Message 업데이트 정보") MessageUpdateDto messageDto
  );

  @Operation(summary = "Message 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Message 삭제 성공"),
      @ApiResponse(
          responseCode = "404",
          description = "Message 를 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<Void> deleteMessage(
      @Parameter(required = true, description = "삭제할 Message UUID") String messageId);


  @Operation(summary = "Channel 의 Message들 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 조회 성공")
  })
  ResponseEntity<PageResponse<MessageResponseDto>> getChannelMessages(
      @Parameter(required = true, description = "조회할 Channel UUID") String channelId,
      Instant cursor,
      Pageable pageable);


}

package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
public interface MessageApi {

  /**
   * 메시지 생성
   **/
  @Operation(summary = "Message 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Message가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = Message.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found"))
      )
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<Message> createMessage(
      @Parameter(
          description = "Message 생성 정보",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      ) CreateMessageRequestDto messageCreateRequest,
      @Parameter(
          description = "Message 첨부 파일들",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
      ) List<MultipartFile> attachments
  );


  /**
   * 메시지 수정
   **/
  @Operation(summary = "Message 내용 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = Message.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found"))
      )
  })
  @PatchMapping("/{messageId}")
  ResponseEntity<Message> updateMessage(
      @Parameter(description = "수정할 Message ID") @PathVariable UUID messageId,
      @Parameter(description = "수정할 Message 내용") @RequestBody UpdateMessageRequestDto request
  );

  /**
   * 메시지 삭제
   **/
  @Operation(summary = "Message 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Message가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found"))
      )
  })
  @DeleteMapping("/{messageId}")
  ResponseEntity<Void> deleteMessage(
      @Parameter(description = "삭제할 Message ID") @PathVariable UUID messageId
  );

  /**
   * 채널의 메시지 목록 조회
   **/
  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class)))
      )
  })
  @GetMapping
  ResponseEntity<List<Message>> findAllByChannelId(
      @Parameter(description = "조회할 Channel ID") @RequestParam("channelId") UUID channelId
  );
}

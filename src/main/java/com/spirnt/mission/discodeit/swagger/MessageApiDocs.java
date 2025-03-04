package com.spirnt.mission.discodeit.swagger;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.enity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageApiDocs {

  // 메시지 전송
  @Operation(summary = "Message 생성", operationId = "create_2")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found")))
  })
  ResponseEntity<Message> createMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @Parameter(description = "Message 첨부 파일들")
      @RequestPart(required = false) List<MultipartFile> attachments);

  // 메시지 수정
  @Operation(summary = "Message 내용 수정", operationId = "update_2")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found")))
  })
  ResponseEntity<Message> updateMessage(
      @Parameter(name = "messageId", description = "수정할 Message ID") @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest);


  // 메시지 삭제
  @Operation(summary = "Message 삭제", operationId = "delete_1")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Message with id {messageId} not found")))
  })
  ResponseEntity<Void> deleteMessage(
      @Parameter(name = "messageId", description = "삭제할 Message ID") @PathVariable UUID messageId);

  // 특정 채널의 메시지 목록 조회
  @Operation(summary = "Channel의 Message 목록 조회", operationId = "findAllByChannelId")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class))))
  })
  ResponseEntity<List<Message>> getAllMessagesByChannel(
      @Parameter(name = "channelId", description = "조회할 Channel ID") @RequestParam UUID channelId);
}

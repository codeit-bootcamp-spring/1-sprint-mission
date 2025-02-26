package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageSwagger {

  @Operation(operationId = "create_2", summary = "Message 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨"),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found")
      ))
  })
  ResponseEntity<Message> sendMessage(
      MessageCreateRequest messageCreateRequest,
      List<MultipartFile> files
  );

  @Operation(operationId = "update_2", summary = "Message 내용 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "Message with id {messageId} not found")
      ))
  })
  ResponseEntity<Message> editMessage(
      UUID messageId,
      MessageUpdateRequest messageUpdateRequest
  );

  @Operation(operationId = "delete_1", summary = "Message 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음", content = @Content(
          examples = @ExampleObject(value = "Message with id {messageId} not found")
      ))
  })
  ResponseEntity<Void> deleteMessage(UUID messageId);

  @Operation(operationId = "findAllByChannelId", summary = "Channel의 Message 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
  })
  ResponseEntity<List<Message>> readAllMessage(UUID channelId);
}

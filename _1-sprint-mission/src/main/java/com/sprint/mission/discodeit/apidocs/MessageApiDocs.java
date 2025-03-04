package com.sprint.mission.discodeit.apidocs;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageApiDocs {

  //메시지 생성
  @Operation(summary = "Message 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "404",
          description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject("Channel | Author with id {channelId | authorId} not found"))
      ),
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨")
  })
  ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments);

  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
  ResponseEntity<List<Message>> findAllByChannelId(
      @Parameter(description = "조회할 Channel ID") @RequestParam UUID channelId);

  @Operation(summary = "Message 내용 수정")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "404",
          description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject("Message with id {messageId} not found"))
      )
  })
  ResponseEntity<Message> update(
      @Parameter(description = "수정할 Message ID") @PathVariable UUID messageId,
      @RequestPart MessageUpdateRequest messageUpdateRequest);

  @Operation(summary = "Message 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
      @ApiResponse(
          responseCode = "404",
          description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject("Message with id {messageId} not found"))
      )
  })
  ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 Message ID") @PathVariable UUID messageId);
}

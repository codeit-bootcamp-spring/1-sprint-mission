package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Message", description = "Message API")
public interface MessageApiDocs {

  @Operation(summary = "Message 생성", description = "새로운 메시지를 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message가 성공적으로 생성됨"),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음")
  })
  ResponseEntity<Message> create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments);

  @Operation(summary = "Message 수정", description = "기존 메시지를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
  })
  ResponseEntity<Message> update(UUID messageId, MessageUpdateRequest request);

  @Operation(summary = "Message 삭제", description = "특정 메시지를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Message가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "Message를 찾을 수 없음")
  })
  ResponseEntity<Void> delete(UUID messageId);

  @Operation(summary = "Channel의 Message 목록 조회", description = "특정 채널의 메시지 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 목록 조회 성공")
  })
  ResponseEntity<List<Message>> findAllByChannelId(UUID channelId);

  ResponseEntity<List<Message>> findAll();
}

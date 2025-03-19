package com.spirnt.mission.discodeit.controller.swagger;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
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

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApiDocs {

  // 특정 채널의 메시지 수신 정보 생성
  @Operation(summary = "Message 읽음 상태 생성", operationId = "create_1")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ReadStatusDto.class))),
      @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재함",
          content = @Content(examples = @ExampleObject(value =
              "ReadStatus with userId {userId} and channelId {channelId} already exists"))),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Channel | User with id {channelId | userId} not found")))
  })
  ResponseEntity<ReadStatusDto> createReadStatus(@RequestBody ReadStatusCreateRequest request);

  // 특정 채널의 메시지 수신 정보 수정
  @Operation(summary = "Message 읽음 상태 수정", operationId = "update_1")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ReadStatusDto.class))),
      @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "ReadStatus with id {readStatusId} not found")))
  })
  ResponseEntity<ReadStatusDto> updateReadStatus(
      @Parameter(name = "readStatusId", description = "수정할 읽음 상태 ID") @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request);

  // 특정 사용자의 메시지 수신 정보 조회
  @Operation(summary = "User의 Message 읽음 상태 목록 조회", operationId = "findAllByUserId")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatusDto.class))))
  })
  ResponseEntity<List<ReadStatusDto>> findAllByUserId(
      @Parameter(name = "userId", description = "조회할 User ID") @RequestParam UUID userId);
}

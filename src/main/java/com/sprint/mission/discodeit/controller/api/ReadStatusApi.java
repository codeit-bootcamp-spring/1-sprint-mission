package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
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

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApi {

  @Operation(summary = "Message 읽음 상태 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ReadStatusDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "채널을 찾을 수 없습니다. | 유저를 찾을 수 없습니다."))
      ),
      @ApiResponse(
          responseCode = "400", description = "이미 읽음 상태가 존재함",
          content = @Content(examples = @ExampleObject(value = "이미 존재하는 메세지 읽음 상태입니다."))
      )
  })
  ResponseEntity<ReadStatusDto> createReadStatus(
      @Parameter(description = "Message 읽음 상태 생성 정보") ReadStatusCreateRequest readStatusCreateRequest
  );

  @Operation(summary = "Message 읽음 상태 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ReadStatusDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "메세지 읽음 상태를 찾을 수 없습니다."))
      )
  })
  ResponseEntity<ReadStatusDto> updateReadStatus(
      @Parameter(description = "수정할 읽음 상태 ID") UUID readStatusId,
      @Parameter(description = "수정할 읽음 상태 정보") ReadStatusUpdateRequest readStatusUpdateRequest
  );

  @Operation(summary = "User의 Message 읽음 상태 목록 조회")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200", description = "Message 읽음 상태 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatusDto.class)))
      )
  )
  ResponseEntity<List<ReadStatusDto>> getReadStatusByUserId(
      @Parameter(description = "조회할 User ID") UUID userId
  );
}

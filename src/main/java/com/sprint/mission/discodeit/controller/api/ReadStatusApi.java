package com.sprint.mission.discodeit.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApi {

  @Operation(summary = "Message 읽음 상태 생성", description = "새로운 읽음 상태를 생성합니다.")
  ResponseEntity<ReadStatusDto> create(@RequestBody String jsonRequest)
      throws JsonProcessingException;

  @Operation(
      summary = "Message 읽음 상태 수정",
      description = "특정 Message 읽음 상태를 업데이트합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태 업데이트 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<ReadStatusDto> update(@Parameter(description = "수정할 읽음 상태 ID", required = true)
      @PathVariable("id") UUID id,
      @RequestBody String jsonRequest) throws JsonProcessingException;

  @Operation(summary = "User의 Message 읽음 상태 목록 조회", description = "사용자의 읽음 상태를 반환합니다.")
  ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId);
}

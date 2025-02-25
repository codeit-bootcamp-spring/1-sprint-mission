package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApiDocs {

  @Operation(summary = "User의 Message 읽음 상태 목록 조회", description = "특정 사용자의 메시지 읽음 상태 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공")
  })
  ResponseEntity<List<ReadStatus>> findAllByUserId(UUID userId);

  @Operation(summary = "Message 읽음 상태 생성", description = "새로운 메시지 읽음 상태를 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨"),
      @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재함"),
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음")
  })
  ResponseEntity<ReadStatus> create(ReadStatusCreateRequest request);

  @Operation(summary = "Message 읽음 상태 수정", description = "기존의 메시지 읽음 상태를 업데이트합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음")
  })
  ResponseEntity<ReadStatus> update(UUID readStatusId, ReadStatusUpdateRequest request);

  ResponseEntity<List<ReadStatus>> findAll();
}

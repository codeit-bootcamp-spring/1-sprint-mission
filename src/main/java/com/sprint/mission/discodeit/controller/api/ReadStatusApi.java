package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/readStatuses")
public interface ReadStatusApi {

  @Operation(summary = "읽음 상태 생성", description = "사용자의 읽음 상태를 새로 생성합니다.")
  @ApiResponse(responseCode = "201", description = "읽음 상태 생성 성공")
  ResponseEntity<ReadStatus> createReadStatus(CreateReadStatusRequest request);

  @Operation(summary = "사용자의 읽음 상태 조회", description = "특정 사용자의 읽음 상태 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "읽음 상태 조회 성공")
  ResponseEntity<List<ReadStatus>> getReadStatusesByUser(UUID userId);

  @Operation(summary = "읽음 상태 ID로 조회", description = "읽음 상태를 Id로 조회합니다.")
  @ApiResponse(responseCode = "200", description = "읽음 상태 조회 성공")
  ResponseEntity<ReadStatus> getReadStatusById(UUID readStatusId);

  @Operation(summary = "읽음 상태 전체 조회", description = "읽음 상태를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "읽음 상태 전체 조회 성공")
  ResponseEntity<List<ReadStatus>> getReadStatuses();

  @Operation(summary = "읽음 상태 ID로 업데이트", description = "읽음 상태를 Id로 업데이트합니다.")
  @ApiResponse(responseCode = "200", description = "읽음 상태 업데이트 성공")
  ResponseEntity<ReadStatus> updateReadStatusById(UUID readStatusId);

  @Operation(summary = "읽음 상태 업데이트", description = "사용자가 특정 채널의 메시지를 읽었음을 표시합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "읽음 상태 업데이트 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자 또는 채널을 찾을 수 없음")
  })
  ResponseEntity<Void> updateReadStatus(
      UUID userId,
      UUID channelId
  );
}

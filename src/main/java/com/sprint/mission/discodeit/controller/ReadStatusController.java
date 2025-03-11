package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "읽음 상태 관리", description = "읽음 상태(Read Status) 관련 API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "읽음 상태 생성", description = "사용자의 읽음 상태를 새로 생성합니다.")
  @ApiResponse(responseCode = "201", description = "읽음 상태 생성 성공")
  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(@RequestBody CreateReadStatusRequest request) {
    ReadStatus readStatus = readStatusService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @Operation(summary = "사용자의 읽음 상태 조회", description = "특정 사용자의 읽음 상태 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "읽음 상태 조회 성공")
  @GetMapping("/{userId}")
  public ResponseEntity<List<ReadStatus>> getReadStatusesByUser(
      @Parameter(description = "읽음 상태를 조회할 사용자 ID", required = true) @PathVariable UUID userId) {
    List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(statuses);
  }

  @Operation(summary = "읽음 상태 ID로 조회", description = "읽음 상태를 Id로 조회합니다.")
  @ApiResponse(responseCode = "200", description = "읽음 상태 조회 성공")
  @GetMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> getReadStatusById(
      @Parameter(description = "읽음 상태 ID 조회", required = true) @PathVariable UUID readStatusId) {
    ReadStatus status = readStatusService.findById(readStatusId);
    return ResponseEntity.ok(status);
  }

  @Operation(summary = "읽음 상태 전체 조회", description = "읽음 상태를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "읽음 상태 전체 조회 성공")
  @GetMapping
  public ResponseEntity<List<ReadStatus>> getReadStatuses() {
    List<ReadStatus> status = readStatusService.findAll();
    return ResponseEntity.ok(status);
  }

  @Operation(summary = "읽음 상태 ID로 업데이트", description = "읽음 상태를 Id로 업데이트합니다.")
  @ApiResponse(responseCode = "200", description = "읽음 상태 업데이트 성공")
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> updateReadStatusById(
      @Parameter(description = "읽음 상태 ID 조회", required = true) @PathVariable UUID readStatusId) {
    ReadStatus status = readStatusService.updateReadStatusById(readStatusId);
    return ResponseEntity.ok(status);
  }

  @Operation(summary = "읽음 상태 업데이트", description = "사용자가 특정 채널의 메시지를 읽었음을 표시합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "읽음 상태 업데이트 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자 또는 채널을 찾을 수 없음")
  })
  @PatchMapping("/{userId}/{channelId}")
  public ResponseEntity<Void> updateReadStatus(
      @Parameter(description = "읽음 상태를 업데이트할 사용자 ID", required = true) @PathVariable UUID userId,
      @Parameter(description = "읽음 상태를 업데이트할 채널 ID", required = true) @PathVariable UUID channelId) {
    readStatusService.updateReadStatusByUserIdAndChannelId(userId, channelId);
    return ResponseEntity.noContent().build();
  }
}

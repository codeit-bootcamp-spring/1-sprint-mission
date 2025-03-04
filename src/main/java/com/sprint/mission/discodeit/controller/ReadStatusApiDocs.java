package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApiDocs {

  @Operation(summary = "Message 읽음 상태 생성", description = "새로운 메시지 읽음 상태를 생성합니다.")
  ResponseEntity<ReadStatus> create(
      @Parameter(description = "읽음 상태 생성 요청 정보", required = true) ReadStatusCreateRequest request
  );

  @Operation(summary = "Message 읽음 상태 수정", description = "기존 메시지 읽음 상태를 수정합니다.")
  ResponseEntity<ReadStatus> update(
      @Parameter(description = "수정할 읽음 상태 ID", required = true) UUID readStatusId,
      @Parameter(description = "수정할 읽음 상태 정보", required = true) ReadStatusUpdateRequest request
  );

  @Operation(summary = "User의 Message 읽음 상태 목록 조회", description = "특정 사용자의 메시지 읽음 상태 목록을 조회합니다.")
  ResponseEntity<List<ReadStatus>> getAllByUserId(
      @Parameter(description = "조회할 User ID", required = true) UUID userId
  );
}
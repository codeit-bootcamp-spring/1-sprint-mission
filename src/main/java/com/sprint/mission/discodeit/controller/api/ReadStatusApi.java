package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public interface ReadStatusApi {

  @Operation(summary = "Message 읽음 상태 생성")
  ResponseEntity<ReadStatusDto> createReadStatus(
      @Parameter(description = "Message 읽음 상태 생성 정보") CreateReadStatusRequest request
  );

  @Operation(summary = "Message 읽음 상태 수정")
  ResponseEntity<ReadStatusDto> updateReadStatus(
      @Parameter(description = "수정할 읽음 상태 ID") UUID readStatusId,
      @Parameter(description = "수정할 읽음 상태 정보") UpdateReadStatusRequest updateReadStatusRequest
  );

  @Operation(summary = "Message 읽음 상태 목록 조회")
  ResponseEntity<List<ReadStatusDto>> findAllReadStatusByUserId(
      @Parameter(description = "Message 읽음 상태 ID") UUID readStatusId
  );
}

package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "ReadStatus API")
public interface ReadStatusControllerDocs {

  @Operation(summary = "readStatus 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "readStatus 생성 성공"),
      @ApiResponse(responseCode = "400", description = "이미 존재하는 readStatus"),
      @ApiResponse(responseCode = "404", description = "채널 혹은 유저를 찾을 수 없음")
  })
  @PostMapping
  ResponseEntity<ReadStatusDto> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  );

  @Operation(summary = "read_status 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "readStatus 조회 성공")
  })
  @GetMapping
  ResponseEntity<List<ReadStatusDto>> find(
      @RequestParam UUID userId
  );

  @Operation(summary = "read_status 수정")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "readStatus 수정 성공"),
      @ApiResponse(responseCode = "404", description = "readStatus를 찾을 수 없음")
  })
  @PatchMapping("/{id}")
  ResponseEntity<ReadStatusDto> update(
      @PathVariable UUID id,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest
  );
}

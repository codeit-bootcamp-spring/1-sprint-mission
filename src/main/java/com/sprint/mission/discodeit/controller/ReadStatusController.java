package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusApi {

  // 메시지 수신 정보에 --> ReadStatus 가 포함된다.
  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @Valid @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
    ReadStatusDto readStatusDto = readStatusService.createReadStatus(readStatusCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusDto);
  }

  @PatchMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatus(
      @PathVariable UUID readStatusId,
      @Valid @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    return ResponseEntity.ok(
        readStatusService.updateReadStatus(readStatusId, readStatusUpdateRequest));
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> getReadStatusByUserId(
      @RequestParam UUID userId) { // 메서드 파라미터 이름과 요청 파라미터 이름이 같으면 자동 매핑
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId)); // 200
  }
}

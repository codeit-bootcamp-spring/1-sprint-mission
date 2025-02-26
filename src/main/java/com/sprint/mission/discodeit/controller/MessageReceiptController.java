package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class MessageReceiptController {

  // 메시지 수신 정보에 --> ReadStatus 가 포함된다.
  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {

    ReadStatus readStatus = readStatusService.createReadStatus(readStatusCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @PutMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatus> updateReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    return ResponseEntity.ok(
        readStatusService.updateReadStatus(readStatusId, readStatusUpdateRequest));
  }

  @GetMapping
  public ResponseEntity<List<ReadStatus>> getReadStatusByUserId(
      @RequestParam UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId)); // 200
  }
}

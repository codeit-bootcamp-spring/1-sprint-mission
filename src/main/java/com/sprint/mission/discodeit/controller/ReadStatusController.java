package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/read-status")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusService.create(request));
  }

  @PutMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> updateReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    return ResponseEntity.ok(readStatusService.update(readStatusId, request));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReadStatus>> getReadStatusesByUser(
      @PathVariable UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }
}

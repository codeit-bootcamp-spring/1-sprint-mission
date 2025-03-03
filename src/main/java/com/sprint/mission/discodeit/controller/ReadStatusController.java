package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
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
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping(value = "")
  public ResponseEntity<ReadStatus> createReadStatus(
      @Valid @RequestBody ReadStatusCreateRequest request) {
    ReadStatus createdReadStatus = readStatusService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }

  @GetMapping(value = "/{userId}")
  public ResponseEntity<List<ReadStatus>> getUserReadStatuses(@PathVariable UUID userId) {
    return ResponseEntity.status(HttpStatus.OK).body(readStatusService.findAllByUserId(userId));
  }

  @PutMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.update(readStatusId, request));
  }

}

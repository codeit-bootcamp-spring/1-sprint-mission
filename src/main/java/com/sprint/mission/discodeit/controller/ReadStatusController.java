package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.read_status.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.read_status.ReadStatusDto;
import com.sprint.mission.discodeit.dto.read_status.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping(value = "")
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @Valid @RequestBody ReadStatusCreateRequest request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatusService.create(request));
  }

  @GetMapping(value = "/{userId}")
  public ResponseEntity<List<ReadStatusDto>> getUserReadStatuses(
      @PathVariable("userId") UUID userId) {
    return ResponseEntity.status(HttpStatus.OK).body(readStatusService.findAllByUserId(userId));
  }

  @PutMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatus(
      @PathVariable("readStatusId") UUID readStatusId,
      @Valid @RequestBody ReadStatusUpdateRequest request) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(readStatusService.update(readStatusId, request));
  }

}

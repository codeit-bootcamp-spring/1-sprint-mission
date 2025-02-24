package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;
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
@RequestMapping("/api/readStatus")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreate readStatusCreate) {
    ReadStatus createdReadStatus = readStatusService.create(readStatusCreate);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }

  @PutMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> update(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdate readStatusUpdate) {
    ReadStatus updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdate);
    return ResponseEntity.ok(updatedReadStatus);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReadStatus>> findAllByUserId(@PathVariable UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatuses);
  }
}

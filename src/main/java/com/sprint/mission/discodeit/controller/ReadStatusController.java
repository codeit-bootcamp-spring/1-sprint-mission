package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read-statuses")
public class ReadStatusController {
  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatusService.create(readStatusCreateRequest));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReadStatus> update(@PathVariable UUID id,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusService.update(id, readStatusUpdateRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<ReadStatus>> getAllByUserId(@PathVariable UUID id) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusService.findAllByUserId(id));
  }
}

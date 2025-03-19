package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusDto> create(@Valid @RequestBody ReadStatusCreateRequest request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatusService.create(request));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ReadStatusDto> update(@PathVariable UUID id,
      @RequestBody ReadStatusUpdateRequest request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusService.update(id, request));
  }

  @GetMapping()
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusService.findAllByUserId(userId));
  }
}

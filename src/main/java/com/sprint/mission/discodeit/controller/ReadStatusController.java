package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @Override
  @PostMapping
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @Valid @RequestBody CreateReadStatusRequestDto request) {
    ReadStatusDto readStatus = readStatusService.create(request);
    return ResponseEntity.ok(readStatus);
  }

  @Override
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatus(@PathVariable UUID readStatusId,
      @Valid @RequestBody UpdateReadStatusRequestDto request) {
    ReadStatusDto updatedReadStatus = readStatusService.update(readStatusId, request);
    return ResponseEntity.ok(updatedReadStatus);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatuses);

  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
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
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @Override
  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(@RequestBody CreateReadStatusRequest request) {
    ReadStatus readStatus = readStatusService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatus);
  }

  @Override
  @GetMapping("/{userId}")
  public ResponseEntity<List<ReadStatus>> getReadStatusesByUser(@PathVariable UUID userId) {
    List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(statuses);
  }

  @Override
  @GetMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> getReadStatusById(@PathVariable UUID readStatusId) {
    ReadStatus status = readStatusService.findById(readStatusId);
    return ResponseEntity.ok(status);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatus>> getReadStatuses() {
    List<ReadStatus> status = readStatusService.findAll();
    return ResponseEntity.ok(status);
  }

  @Override
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> updateReadStatusById(@PathVariable UUID readStatusId) {
    ReadStatus status = readStatusService.updateReadStatusById(readStatusId);
    return ResponseEntity.ok(status);
  }

  @Override
  @PatchMapping("/{userId}/{channelId}")
  public ResponseEntity<Void> updateReadStatus(
      @PathVariable UUID userId,
      @PathVariable UUID channelId
  ) {
    readStatusService.updateReadStatusByUserIdAndChannelId(
        userId,
        channelId);
    return ResponseEntity.noContent()
        .build();
  }
}

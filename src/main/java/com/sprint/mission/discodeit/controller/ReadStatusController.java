package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping("/read-statuses")
  public ResponseEntity<ReadStatus> createReadStatus(@RequestBody CreateReadStatusDto dto){
    ReadStatus status = readStatusService.create(dto, false);
    return ResponseEntity.ok(status);
  }

  @PatchMapping("/read-statuses/{id}")
  public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable String id, @RequestBody CreateReadStatusDto dto){
    ReadStatus status = readStatusService.update(dto);
    return ResponseEntity.ok(status);
  }

  @GetMapping("/users/{userId}/read-statuses")
  public ResponseEntity<List<ReadStatus>> getUserReadStatus(@PathVariable String userId){
    List<ReadStatus> status = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(status);
  }
}

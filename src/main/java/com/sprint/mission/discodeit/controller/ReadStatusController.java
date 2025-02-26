package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
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
@RequestMapping("/api/receipts")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  // 메시지 수신 정보 생성
  @PostMapping
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
    ReadStatusDto readStatusDto = readStatusService.create(readStatusCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusDto);
  }

  // 메시시 수신 정보 수정
  @PutMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatus(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatusDto readStatusDto = readStatusService.update(readStatusId, readStatusUpdateRequest);
    return ResponseEntity.ok(readStatusDto);
  }

  // 특정 사용자의 메시지 수신 정보 조회
  @GetMapping("/users/{userId}")
  public ResponseEntity<List<ReadStatusDto>> getReadStatusesOfUser(
      @PathVariable UUID userId) {
    List<ReadStatusDto> readStatusDtoList = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatusDtoList);
  }
}

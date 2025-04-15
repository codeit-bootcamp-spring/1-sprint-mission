package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.read_status.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.read_status.ReadStatusDto;
import com.sprint.mission.discodeit.dto.read_status.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping(value = "/readStatuses")
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @Valid @RequestBody ReadStatusCreateRequest request) {
    log.info("읽음 상태 생성 요청: {}", request);
    ReadStatusDto createReadStatus = readStatusService.create(request);
    log.debug("읽음 상태 생성 응답: {}", createReadStatus);
    return ResponseEntity.status(HttpStatus.CREATED).body(createReadStatus);
  }

  @PutMapping(value = "/readStatuses/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatus(
      @PathVariable("readStatusId") UUID readStatusId,
      @Valid @RequestBody ReadStatusUpdateRequest request) {
    log.info("읽음 상태 수정 요청: id={}, request={}", readStatusId, request);
    ReadStatusDto updateReadStatus = readStatusService.update(readStatusId, request);
    log.debug("읽음 상태 수정 응답: {}", updateReadStatus);
    return ResponseEntity.status(HttpStatus.OK).body(updateReadStatus);
  }

  @GetMapping(value = "/readStatuses")
  public ResponseEntity<List<ReadStatusDto>> getUserReadStatuses(
      @RequestParam("userId") UUID userId) {
    log.info("사용자별 읽음 상태 목록 조회 요청: userId={}", userId);
    List<ReadStatusDto> readStatusList = readStatusService.findAllByUserId(userId);
    log.debug("사용자별 읽음 상태 목록 조회 응답: count={}", readStatusList.size());
    return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
  }


}

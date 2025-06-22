package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusDto> create(@RequestBody @Valid ReadStatusCreateRequest request) {
    log.info("읽음 상태 생성 요청: {}", request);
    ReadStatusDto createdReadStatus = readStatusService.create(request);
    log.debug("읽음 상태 생성 응답: {}", createdReadStatus);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }

  @PatchMapping(path = "{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(@PathVariable("readStatusId") UUID readStatusId,
      @RequestBody @Valid ReadStatusUpdateRequest request) {
    log.info("읽음 상태 수정 요청: id={}, request={}", readStatusId, request);
    ReadStatusDto updatedReadStatus = readStatusService.update(readStatusId, request);
    log.debug("읽음 상태 수정 응답: {}", updatedReadStatus);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedReadStatus);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    log.info("사용자별 읽음 상태 목록 조회 요청: userId={}", userId);
    List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);
    log.debug("사용자별 읽음 상태 목록 조회 응답: count={}", readStatuses.size());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }
}

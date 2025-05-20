package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @Override
  @PostMapping
  public ResponseEntity<ReadStatusDto> create(
      @Valid @RequestBody CreateReadStatusRequest request) {

    log.info("읽음 상태 생성 요청: {}", request);
    ReadStatusDto readStatusDto = readStatusService.create(request);
    log.debug("읽음 상태 생성 응답: {}", readStatusDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatusDto);
  }

  @Override
  @PatchMapping(path = "{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(
      @PathVariable("readStatusId") UUID readStatusId,
      @Valid @RequestBody UpdateReadStatusRequest request) {

    log.info("읽음 상태 수정 요청: id={}, request={}", readStatusId, request);
    ReadStatusDto readStatusDto = readStatusService.update(readStatusId, request);
    log.debug("읽음 상태 수정 응답: {}", readStatusDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusDto);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
      @RequestParam("userId") UUID userId) {

    log.info("사용자별 읽음 상태 목록 조회 요청: userId={}", userId);
    List<ReadStatusDto> readStatusDtos = readStatusService.findAllByUserId(userId);
    log.debug("사용자별 읽음 상태 목록 조회 응답: count={}", readStatusDtos.size());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusDtos);
  }
}

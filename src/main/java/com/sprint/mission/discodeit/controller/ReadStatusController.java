package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
      @RequestBody CreateReadStatusRequest request) {

    log.info("ReadStatus 생성 요청 : userId={}, channelId={}", request.userId(), request.channelId());

    ReadStatusDto readStatusDto = readStatusService.create(request);

    log.info("ReadStatus 생성 성공 : readStatusId={}", readStatusDto.id());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatusDto);
  }

  @Override
  @PatchMapping(path = "{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody UpdateReadStatusRequest request) {

    log.info("ReadStatus 수정 요청 : readStatusId={}", readStatusId);

    ReadStatusDto readStatusDto = readStatusService.update(readStatusId, request);

    log.info("ReadStatus 수정 성공 : readStatusId={}", readStatusId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusDto);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
      @RequestParam("userId") UUID userId) {

    log.info("ReadStatus 다건 조회 요청 : userId={}", userId);

    List<ReadStatusDto> readStatusDtos = readStatusService.findAllByUserId(userId);

    log.info("ReadStatus 다건 조회 성공 : 반환 개수={}", readStatusDtos.size());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusDtos);
  }
}

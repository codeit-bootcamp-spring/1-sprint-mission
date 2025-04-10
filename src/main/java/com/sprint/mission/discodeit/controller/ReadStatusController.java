package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readstatus")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;


  // 메시지 수신 정보 생성 (POST /readstatus)
  @PostMapping
  public ResponseEntity<ReadStatusDto> create(
      @RequestBody @Valid ReadStatusRequest readStatusRequest) {
    return ResponseEntity.ok(readStatusService.create(readStatusRequest));
  }

  // 메시지 수신 정보 업데이트 (PUT /readstatus/{readStatusId})
  @PutMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody @Valid ReadStatusUpdateRequest readStatusUpdateRequest) {
    //TODO: readStatusUpdateDTO의 Instant가 아닌 readStatus 엔티티에서 현재시각으로 업데이트가됨
    // 인자 전달로 수정 필요
    return ResponseEntity.ok(readStatusService.update(readStatusId, readStatusUpdateRequest));
  }

  // 특정 사용자의 메시지 수신 정보 조회 (GET /readstatus/user/{userId})
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@PathVariable("userId") UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }
}

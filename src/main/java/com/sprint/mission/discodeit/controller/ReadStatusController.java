package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResponseDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseDTO<ReadStatus> create(@RequestBody ReadStatusCreateDTO request) {
    return ResponseDTO.<ReadStatus>builder()
        .code(HttpStatus.CREATED.value())
        .message("메시지 수신정보 생성 완료")
        .data(readStatusService.create(request))
        .build();
  }

  @PutMapping("{readStatusId}")
  public ResponseDTO<ReadStatus> update(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateDTO request) {
    return ResponseDTO.<ReadStatus>builder()
        .code(HttpStatus.OK.value())
        .message("메시지 수신정보 수정 완료")
        .data(readStatusService.update(readStatusId, request))
        .build();
  }

  @GetMapping
  public ResponseDTO<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
    return ResponseDTO.<List<ReadStatus>>builder()
        .code(HttpStatus.OK.value())
        .message("특정 사용자의 메시지 수신 정보를 조회")
        .data(readStatusService.findAllByUserId(userId))
        .build();
  }

}

package com.sprint.mission.controller;

import com.sprint.mission.common.CommonResponse;
import com.sprint.mission.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.service.jcf.addOn.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  // 카피
  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<CommonResponse> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus createdReadStatus = readStatusService.create(request);
    return CommonResponse.toResponseEntity
        (CREATED, "읽음 상태가 생성되었습니다.", createdReadStatus);
  }

  @PatchMapping("{id}")
  public ResponseEntity<CommonResponse> update(@RequestParam("id") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
    return CommonResponse.toResponseEntity
        (OK, "읽음 상태가 업데이트되었습니다.", updatedReadStatus);
  }

  @GetMapping
  public ResponseEntity<CommonResponse> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return CommonResponse.toResponseEntity
        (OK, "읽음 상태 목록이 조회되었습니다.", readStatuses);
  }
}

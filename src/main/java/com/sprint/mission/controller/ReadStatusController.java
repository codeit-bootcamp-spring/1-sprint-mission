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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatus")
public class ReadStatusController {

  // 카피
  private final ReadStatusService readStatusService;

  @RequestMapping(path = "create")
  public ResponseEntity<CommonResponse> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus createdReadStatus = readStatusService.create(request);
    return CommonResponse.toResponseEntity
        (CREATED, "읽음 상태가 생성되었습니다.", createdReadStatus);
  }

  @RequestMapping(path = "update")
  public ResponseEntity<CommonResponse> update(@RequestParam("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
    return CommonResponse.toResponseEntity
        (OK, "읽음 상태가 업데이트되었습니다.", updatedReadStatus);
  }

  @RequestMapping(path = "findAllByUserId")
  public ResponseEntity<CommonResponse> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return CommonResponse.toResponseEntity
        (OK, "읽음 상태 목록이 조회되었습니다.", readStatuses);
  }
}

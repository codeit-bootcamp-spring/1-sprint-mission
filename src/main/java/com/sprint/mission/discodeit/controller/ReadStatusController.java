package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.status.read.ReadStatusCreateResponse;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusModifyRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusModifyResponse;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusResponse;
import com.sprint.mission.discodeit.service.status.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  /**
   * user의 메세지 읽음 상태 목록 조회
   */
  @GetMapping
  public List<ReadStatusResponse> findAllUserReadStatus(@RequestParam("userId") UUID userId) {
    return readStatusService.findByUserId(userId);
  }

  /**
   * 읽음 상태 생성
   */
  @PostMapping
  public ReadStatusCreateResponse createReadStatus(@RequestBody ReadStatusRequest request) {
    return readStatusService.createReadStatus(request);
  }

  /**
   * 읽음 상태 수정
   */
  @PatchMapping("/{readStatusId}")
  public ReadStatusModifyResponse updateReadStatus(@PathVariable("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusModifyRequest request) {

    return readStatusService.updateReadStatus(readStatusId, request);
  }
}

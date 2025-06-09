package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.status.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Read Status Controller")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  /**
   * 읽음 상태 생성
   */
  @Operation(summary = "읽음 상태 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("#request.userId() == authentication.principal.id")
  @PostMapping
  public ReadStatusDto createReadStatus(@Validated @RequestBody ReadStatusRequest request) {
    return readStatusService.create(request);
  }

  /**
   * user의 메세지 읽음 상태 목록 조회
   */
  @Operation(summary = "유저의 메세지 읽은 상태 목록 조회")
  @GetMapping
  public List<ReadStatusDto> findAllUserReadStatus(@RequestParam("userId") UUID userId) {
    return readStatusService.findByUserId(userId);
  }

  /**
   * 읽음 상태 수정
   */
  @Operation(summary = "읽음 상태 수정")
  @PreAuthorize("@authorizationChecker.isReadStatusUser(#readStatusId, authentication.principal.id)")
  @PatchMapping("/{readStatusId}")
  public ReadStatusDto updateReadStatus(
      @P("readStatusId") @PathVariable("readStatusId") UUID readStatusId,
      @Validated @RequestBody ReadStatusUpdateRequest request) {

    return readStatusService.update(readStatusId, request);
  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.security.AccessManager;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;
  private final AccessManager accessManager;

  //@PreAuthorize("@accessManager.isSelf(#request.userId(), authentication)")
  @PostMapping
  public ResponseEntity<ReadStatusDto> create(
          @Valid @RequestBody ReadStatusCreateDTO request,
          Authentication authentication) {

    if (!accessManager.isSelf(request.getUserId(), authentication)) {
      throw new AccessDeniedException("권한 없음");
    }


    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatusService.create(request));
  }

  @PreAuthorize("@accessManager.isReadStatusOwner(#readStatusId, authentication)")
  @PatchMapping("{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusService.update(readStatusId, request));
  }


  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusService.findAllByUserId(userId));
  }
}

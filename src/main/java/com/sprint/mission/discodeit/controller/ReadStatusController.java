package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class ReadStatusController implements ReadStatusControllerDocs {

  private final ReadStatusService readStatusService;

  @PreAuthorize("#readStatusReqDto.userId() == authentication.principal.id")
  @PostMapping
  public ResponseEntity<ReadStatusResponseDto> createReadStatus(
      @Valid @RequestBody ReadStatusRequestDto readStatusReqDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusService.create(readStatusReqDto));
  }

  @PreAuthorize("#userDetails.user.id == authentication.principal.id")
  @PatchMapping(path = "/{readStatusId}")
  public ResponseEntity<ReadStatusResponseDto> updateReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequestDto readStatusUpdateRequest,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(
        readStatusService.update(readStatusId, readStatusUpdateRequest));
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusResponseDto>> getReadStatusByUser(
      @RequestParam UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }
}

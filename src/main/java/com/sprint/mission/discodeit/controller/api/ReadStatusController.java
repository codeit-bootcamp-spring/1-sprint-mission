package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.ReadStatusApiDocs;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read-status")
public class ReadStatusController implements ReadStatusApiDocs {

  private final ReadStatusService readStatusService;

  @PostMapping
  @Override
  public ResponseEntity<CustomApiResponse<ReadStatusResponse>> createReadStatus(
      @RequestBody ReadStatusRequest.Create readStatusRequest) {

    log.info("POST /api/read-status");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CustomApiResponse.created(readStatusService.create(readStatusRequest)));
  }

  @PutMapping("/{readStatusId}")
  @Override
  public ResponseEntity<CustomApiResponse<ReadStatusResponse>> updateReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusRequest.Update readStatusRequest) {

    log.info("PUT /api/read-status/{}", readStatusId);
    return ResponseEntity.ok(
        CustomApiResponse.success(readStatusService.update(readStatusId, readStatusRequest))
    );
  }

  @GetMapping
  @Override
  public ResponseEntity<CustomApiResponse<List<ReadStatusResponse>>> getReadStatusByUser(
      @RequestParam("userId") UUID userId) {
    return ResponseEntity.ok(CustomApiResponse.success(readStatusService.findAllByUserId(userId)));
  }
}

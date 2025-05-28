package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.ReadStatusApiDocs;
import com.sprint.mission.discodeit.global.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApiDocs {

    private final ReadStatusService readStatusService;

    @PostMapping
    @Override
    public ResponseEntity<ReadStatusResponse> createReadStatus(
        @Valid @RequestBody ReadStatusRequest.Create readStatusRequest) {

        log.info("POST /api/readStatuses");
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(readStatusService.create(readStatusRequest));
    }

    @PatchMapping("/{readStatusId}")
    @Override
    public ResponseEntity<ReadStatusResponse> updateReadStatus(
        @PathVariable UUID readStatusId,
        @Valid @RequestBody ReadStatusRequest.Update readStatusRequest) {

        log.info("PUT /api/readStatuses/{}", readStatusId);
        return ResponseEntity.ok(readStatusService.update(readStatusId, readStatusRequest));
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ReadStatusResponse>> getReadStatusByUser(
        @RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}

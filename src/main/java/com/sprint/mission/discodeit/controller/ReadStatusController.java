package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
        return ResponseEntity.ok(readStatusService.create(request));
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> getUserReadStatuses(@PathVariable UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }

    @RequestMapping(value = "/update/{readStatusId}", method = RequestMethod.PUT)
    public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdateRequest request) {
        return ResponseEntity.ok(readStatusService.update(readStatusId, request));
    }

}

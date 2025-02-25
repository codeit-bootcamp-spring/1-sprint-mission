package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/read-statuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
        ReadStatus createdReadStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReadStatus);
    }

    @PutMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> updateReadStatus(
            @PathVariable("readStatusId") UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);

        if (updatedReadStatus == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedReadStatus);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ReadStatus>> getReadStatusesByUser(@PathVariable("userId") UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(readStatuses);
    }
}
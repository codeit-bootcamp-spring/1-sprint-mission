package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/read-status")
public class MessageReadController {
    private final ReadStatusService readStatusService;

    @PostMapping("/create")
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
        ReadStatus createdReadStatus = readStatusService.create(request);
        return ResponseEntity.ok(createdReadStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadStatus> getReadStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(readStatusService.find(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatus>> getReadStatusesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReadStatus> updateReadStatus(
            @PathVariable UUID id,
            @RequestBody ReadStatusUpdateRequest request
    ) {
        ReadStatus updatedReadStatus = readStatusService.update(id, request);
        return ResponseEntity.ok(updatedReadStatus);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReadStatus(@PathVariable UUID id) {
        readStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

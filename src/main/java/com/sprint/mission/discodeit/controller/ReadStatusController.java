package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/read-statuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusDto> create(@RequestBody ReadStatusCreateRequest request) {
        ReadStatusDto createdReadStatus = readStatusService.create(request);
        return ResponseEntity.ok(createdReadStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadStatus> get(@PathVariable("id") UUID readStatusId) {
        ReadStatus readStatus = readStatusService.find(readStatusId);
        return ResponseEntity.ok(readStatus);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatus>> getAllByUserId(@PathVariable("userId") UUID userId) {
        List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(statuses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadStatus> update(@PathVariable("id") UUID readStatusId,
                                             @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus updatedStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID readStatusId) {
        readStatusService.delete(readStatusId);
        return ResponseEntity.noContent().build();
    }
}
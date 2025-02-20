package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;
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
    private ReadStatusService readStatusService;
    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreate readStatusCreate) {
        ReadStatus readStatus = readStatusService.create(readStatusCreate);
        return ResponseEntity.ok(readStatus);
    }

    @GetMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> getReadStatusById(@PathVariable UUID readStatusId) {
        ReadStatus readStatus = readStatusService.findById(readStatusId);
        return ResponseEntity.ok(readStatus);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatus>> getReadStatusByUser(@PathVariable UUID userId){
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(readStatuses);
    }

    @PutMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> updateReadStatus (@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdate readStatusUpdate) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdate);
        return ResponseEntity.ok(updatedReadStatus);
    }

    @DeleteMapping("/{readStatusId}")
    public ResponseEntity<Void> deleteReadStatus(@PathVariable UUID readStatusId) {
        readStatusService.delete(readStatusId);
        return ResponseEntity.noContent().build();
    }




}

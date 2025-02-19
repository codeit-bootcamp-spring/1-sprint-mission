package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping("/channel/{channelId}")
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusDTO dto) {
        return ResponseEntity.ok(readStatusService.create(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID id, @RequestBody ReadStatusDTO dto) {
        return ResponseEntity.ok(readStatusService.update(id,dto));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatus>> getReadStatus(@PathVariable UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}

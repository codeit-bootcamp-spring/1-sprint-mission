package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/read-status")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody CreateReadStatusRequestDto request) {
        ReadStatus readStatus = readStatusService.create(request);
        return ResponseEntity.ok(readStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID id, @RequestBody UpdateReadStatusRequestDto request) {
        ReadStatus updatedReadStatus = readStatusService.update(id, request);
        return ResponseEntity.ok(updatedReadStatus);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatus>> getReadStatusByUserId(@PathVariable UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserid(userId);
        return ResponseEntity.ok(readStatuses);
    }

}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @GetMapping
    public ResponseEntity<List<ReadStatus>> getStatusesByUserId(@RequestParam("id") UUID userID) {
        List<ReadStatus> allByUserId = readStatusService.findAllByUserId(userID);
        return ResponseEntity.ok(allByUserId);
    }

    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody CreateReadStatusRequest request) {
        ReadStatus readStatus = readStatusService.create(request);
        return ResponseEntity.ok(readStatus);
    }
}

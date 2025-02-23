package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest request) {
        ReadStatus readStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
    }

    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PUT)
    public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(updatedReadStatus);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> getReadStatusForUser(@PathVariable UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(readStatuses);
    }
}

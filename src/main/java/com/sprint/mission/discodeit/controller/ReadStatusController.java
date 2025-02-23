package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read_statuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ReadStatusRequest readStatusRequest) {
        ReadStatus readStatus = readStatusService.create(readStatusRequest);
        return ResponseEntity.created(URI.create("/read_statuses/" + readStatus.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadStatusResponse> find(@PathVariable UUID id) {
        ReadStatus readStatus = readStatusService.find(id);
        return ResponseEntity.ok(ReadStatusResponse.from(readStatus));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id) {
        readStatusService.update(id);
        return ResponseEntity.ok().build();
    }
}

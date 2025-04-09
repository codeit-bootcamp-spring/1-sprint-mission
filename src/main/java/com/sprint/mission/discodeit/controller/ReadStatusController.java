package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @Operation(summary = "Message 읽음 상태 목록 조회", description = "User의 Message 읽음 상태 목록 조회")
    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam UUID userId) {
        List<ReadStatusDto> statuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(statuses);
    }

    @Operation(summary = "Message 읽음 상태 생성", description = "Message 읽음 상태 생성")
    @PostMapping
    public ResponseEntity<ReadStatusDto> create(@RequestBody ReadStatusCreateRequest request) {
        ReadStatusDto createdReadStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReadStatus);
    }

    @Operation(summary = "Message 읽음 상태 조회", description = "단일 읽음 상태 조회")
    @GetMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusDto> get(@PathVariable("readStatusId") UUID readStatusId) {
        ReadStatusDto readStatus = readStatusService.find(readStatusId);
        return ResponseEntity.ok(readStatus);
    }

    @Operation(summary = "Message 읽음 상태 수정", description = "Message 읽음 상태 수정")
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusDto> update(
            @PathVariable("readStatusId") UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request) {
        ReadStatusDto updatedStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(updatedStatus);
    }

    @Operation(summary = "Message 읽음 상태 삭제", description = "Message 읽음 상태 삭제")
    @DeleteMapping("/{readStatusId}")
    public ResponseEntity<Void> delete(@PathVariable("readStatusId") UUID readStatusId) {
        readStatusService.delete(readStatusId);
        return ResponseEntity.noContent().build();
    }
}
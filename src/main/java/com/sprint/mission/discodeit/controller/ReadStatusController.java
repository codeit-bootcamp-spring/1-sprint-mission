package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusAPI;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusAPI {
    private ReadStatusService readStatusService;
    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreate readStatusCreate) {
        ReadStatus readStatus = readStatusService.create(readStatusCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
    }

    @PatchMapping("{readStatusId}")
    public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdate readStatusUpdate) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatus>> getReadStatusByUser(@PathVariable UUID userId){
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(readStatuses);
    }

}

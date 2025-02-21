package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/readStatus")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(path = "create")
    public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreate readStatusCreate) {
        ReadStatus createdReadStatus = readStatusService.create(readStatusCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReadStatus);
    }

    @RequestMapping(path = "update")
    public ResponseEntity<ReadStatus> update(@RequestParam("readStatusId") UUID readStatusId,
                                             @RequestBody ReadStatusUpdate readStatusUpdate) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedReadStatus);
    }

    @RequestMapping(path = "findAllByUserId")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatuses);
    }
}

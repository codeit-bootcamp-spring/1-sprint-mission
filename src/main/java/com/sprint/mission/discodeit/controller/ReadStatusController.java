package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/readStatus")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(path = "create")
    public ResponseEntity<ReadStatus> create(@ModelAttribute ReadStatusCreateRequest request) {
        ReadStatus createdReadStatus = readStatusService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReadStatus);
    }

    @RequestMapping(path = "update")
    public ResponseEntity<Void> update(@RequestParam("readStatusId") UUID readStatusId,
                                             @ModelAttribute ReadStatusUpdateRequest request) {
        readStatusService.update(readStatusId, request);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @RequestMapping(path = "findAllByUserId")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatuses);
    }
}
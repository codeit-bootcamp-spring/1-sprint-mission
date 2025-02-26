package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponse> create(
            @RequestParam("userid") UUID userId,
            @RequestParam("channelid") UUID channelId,
            @RequestParam("messageid") UUID messageId
    ) {
        ReadStatus createdReadStatus = readStatusService.create(userId, channelId, messageId);
        ReadStatusResponse response = ReadStatusResponse.from(createdReadStatus);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ReadStatusResponse> findById(@RequestParam("id") UUID id) {
        ReadStatus readStatus = readStatusService.findById(id);
        if (readStatus == null) {
            throw new NoSuchElementException("read status를 찾을 수 없습니다: " + id);
        }
        ReadStatusResponse response = ReadStatusResponse.from(readStatus);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @RequestMapping(value = "/listByUser", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponse>> findAllByUserId(@RequestParam("userid") UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        List<ReadStatusResponse> responses = new ArrayList<>();
        for (ReadStatus readStatus : readStatuses) {
            responses.add(ReadStatusResponse.from(readStatus));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<ReadStatusResponse> update(
            @RequestParam("id") UUID id,
            @RequestBody ReadStatusUpdateRequest request
    ) {
        ReadStatus updatedReadStatus = readStatusService.update(id, request);
        if (updatedReadStatus == null) {
            throw new NoSuchElementException("read status를 찾을 수 없습니다: " + id);
        }
        ReadStatusResponse response = ReadStatusResponse.from(updatedReadStatus);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@RequestParam("id") UUID id) {
        ReadStatus readStatus = readStatusService.findById(id);
        if (readStatus == null) {
            throw new NoSuchElementException("read status를 찾을 수 없습니다: " + id);
        }
        readStatusService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
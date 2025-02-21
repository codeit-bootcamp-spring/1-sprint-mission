package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ReadStatus> createReadStatus(
        @RequestBody ReadStatusCreateRequest readStatusCreateRequest
    ) {
        ReadStatus createdReadStatus = readStatusService.create(readStatusCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReadStatus);
    }

    @RequestMapping(value = "/{readStatusId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        method = RequestMethod.PATCH)
    public ResponseEntity<ReadStatus> updateReadStatus(
        @PathVariable UUID readStatusId,
        @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest
    ) {

        ReadStatus updatedReadStatus = readStatusService.update(
            readStatusId, readStatusUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> readReadStatus(@PathVariable UUID userId) {

        List<ReadStatus> allReadStatus = readStatusService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(allReadStatus);
    }
}

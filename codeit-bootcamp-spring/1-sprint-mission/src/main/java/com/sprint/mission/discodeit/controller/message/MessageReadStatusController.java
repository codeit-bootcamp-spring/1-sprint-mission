package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/read-status")
public class MessageReadStatusController {

    private final ReadStatusService readStatusService;

    public MessageReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> findByUserId(
            @PathVariable(name = "userId") UUID userId
    ) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createReadStatus(
            @RequestBody ReadStatusCreateRequest readStatusCreateRequest
    ) {
        readStatusService.create(readStatusCreateRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateReadStatusByChannelId(
            @PathVariable(name = "userId") UUID userId,
            @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest
    ) {
        List<ReadStatus> allByUserId = readStatusService.findAllByUserId(userId);
        allByUserId.stream().map(ReadStatus::getId).forEach(it -> readStatusService.update(it, readStatusUpdateRequest));
        return ResponseEntity.noContent().build();
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping
    public ReadStatus createReadStatus(@RequestBody CreateReadStatusRequest request) {
        return readStatusService.create(request);
    }

    @GetMapping("/user/{userId}")
    public List<ReadStatus> getReadStatusesByUser(@PathVariable UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }

    @PatchMapping("/{userId}/{channelId}")
    public void updateReadStatus(@PathVariable UUID userId, @PathVariable UUID channelId) {
        readStatusService.updateReadStatus(userId, channelId);
    }
}

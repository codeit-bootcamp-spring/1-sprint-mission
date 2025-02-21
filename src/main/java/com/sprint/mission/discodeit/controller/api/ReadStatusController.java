package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping
    public ReadStatusResponse createReadStatus(@RequestBody ReadStatusRequest readStatusRequest) {
        return readStatusService.create(readStatusRequest);
    }

    @PutMapping("/{readStatusId}")
    public ReadStatusResponse updateReadStatus(@PathVariable UUID readStatusId) {
        return readStatusService.update(readStatusId);
    }

    @GetMapping
    public List<ReadStatusResponse> getReadStatusByUser(@RequestParam("userId") UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.docs.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-statuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusControllerDocs {
    private final ReadStatusService readStatusService;

    @PostMapping
    public CustomApiResponse<ReadStatusResponseDto> createReadStatus(@RequestBody ReadStatusRequestDto readStatusReqDto) {
        return CustomApiResponse.created(readStatusService.create(readStatusReqDto));
    }

    @GetMapping(path = "/{readStatusId}")
    public CustomApiResponse<ReadStatusResponseDto> updateReadStatus(@PathVariable UUID readStatusId, @RequestParam("lastReadAt") Instant lastReadAt) {
        return CustomApiResponse.ok(readStatusService.update(readStatusId, lastReadAt));
    }

    @GetMapping
    public CustomApiResponse<List<ReadStatusResponseDto>> getReadStatusByUser(@RequestParam UUID userId) {
        return CustomApiResponse.ok(readStatusService.findAllByUserId(userId));
    }
}

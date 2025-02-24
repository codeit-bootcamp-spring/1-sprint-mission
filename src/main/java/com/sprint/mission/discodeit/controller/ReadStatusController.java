package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse<ReadStatusDTO.idResponse> createReadStatus(@RequestBody ReadStatusDTO.request readStatusReqDto) {
        return ApiResponse.created(readStatusService.create(readStatusReqDto));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResponse<ReadStatusDTO.idResponse> updateReadStatus(@PathVariable Long id, @RequestParam("lastReadAt") Instant lastReadAt) {
        return ApiResponse.ok(readStatusService.update(ReadStatusDTO.update.builder()
                .id(id).lastReadAt(lastReadAt).build()
        ));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse<List<ReadStatusDTO.response>> getReadStatusByUser(@RequestParam UUID userId) {
        return ApiResponse.ok(readStatusService.findAllByUserId(userId));
    }
}

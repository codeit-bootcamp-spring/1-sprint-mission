package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusService.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.ReadStatusService.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.readStatus.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/read-status")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    // ✅ 특정 채널의 메시지 수신 정보 생성 (POST /read-status/create)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ReadStatus createReadStatus(@RequestBody ReadStatusCreateRequestDTO request) {
        return readStatusService.create(request);
    }

    // ✅ 특정 메시지 수신 정보 조회 (GET /read-status/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ReadStatus getReadStatus(@PathVariable UUID id) {
        return readStatusService.find(id);
    }

    // ✅ 특정 사용자의 모든 메시지 수신 정보 조회 (GET /read-status/user/{userId})
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<ReadStatus> getReadStatusByUser(@PathVariable UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }

    // ✅ 특정 채널의 메시지 수신 정보 수정 (PUT /read-status/update)
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ReadStatus updateReadStatus(@RequestBody ReadStatusUpdateRequestDTO request) {
        return readStatusService.update(request);
    }

    // ✅ 특정 메시지 수신 정보 삭제 (DELETE /read-status/{id})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteReadStatus(@PathVariable UUID id) {
        readStatusService.delete(id);
    }
}

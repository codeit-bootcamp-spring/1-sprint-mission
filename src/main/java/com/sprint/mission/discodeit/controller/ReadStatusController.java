package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    // 메시지 수신(읽음) 정보 생성
    @RequestMapping(method = RequestMethod.POST)
    public ReadStatusDto createReadStatus(@RequestBody ReadStatusDto readStatusDto) {
        return readStatusService.createReadStatus(
                readStatusDto.getUserId(),
                readStatusDto.getChannelId(),
                readStatusDto.getLastReadAt()
        );
    }

    // 특정 사용자와 채널의 마지막 읽은 메시지 조회
    @RequestMapping(value = "/{userId}/channel/{channelId}", method = RequestMethod.GET)
    public ReadStatusDto getLastReadStatus(@PathVariable("userId") UUID userId,
                                           @PathVariable("channelId") UUID channelId) {
        return readStatusService.findLastReadMessage(userId, channelId);
    }

    // ReadStatus 수정 (lastReadAt 업데이트)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateLastReadAt(@PathVariable("id") UUID id, @RequestBody Instant newTime) {
        readStatusService.updateLastReadAt(id, newTime);
    }

    // ReadStatus 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteReadStatus(@PathVariable("id") UUID id) {
        readStatusService.deleteReadStatus(id);
    }
}

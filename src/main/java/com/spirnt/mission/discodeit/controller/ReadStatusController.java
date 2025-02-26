package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.ErrorResponse;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusResponse;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readstatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    // 특정 채널의 메시지 수신 정보 생성
    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    public ResponseEntity<?> createReadStatus(@RequestBody ReadStatusCreate request) {
        ReadStatus readStatus = readStatusService.create(request);
        ReadStatusResponse response = new ReadStatusResponse(readStatus.getId(),
                readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 특정 채널의 메시지 수신 정보 수정
    @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateReadStatus(@PathVariable UUID readStatusId,
                                              @RequestBody ReadStatusUpdate request) {
        // 읽은 시간을 미래로 설정하려는 경우
        if (request.lastReadAt().isAfter(Instant.now())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Invalid Time Statement"));
        }
        ReadStatus readStatus = readStatusService.update(readStatusId, request);
        ReadStatusResponse response = new ReadStatusResponse(readStatus.getId(),
                readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadAt());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 사용자의 메시지 수신 정보 조회
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllByUserId(@PathVariable UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        List<ReadStatusResponse> readStatusResponses = readStatuses.stream()
                .map(readStatus -> new ReadStatusResponse(readStatus.getId(),
                        readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(readStatusResponses);
    }
}

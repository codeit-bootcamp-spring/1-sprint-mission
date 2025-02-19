package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResponseDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping("/readStatus")
    public ResponseDTO<UUID> createReadStatus(@RequestBody ReadStatusCreateDTO request){
        return ResponseDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("메시지 수신정보 생성 완료")
                .data(readStatusService.create(request))
                .build();
    }

    @PutMapping("/readStatus/{readStatusId}")
    public ResponseDTO updateReadStatus(@PathVariable UUID readStatusId,
                                        @RequestBody ReadStatusUpdateDTO request){
        readStatusService.update(readStatusId, request);
        return ResponseDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("메시지 수신정보 수정 완료")
                .build();
    }

    @GetMapping("readStatus/{userId}")
    public ResponseDTO<List<ReadStatus>> getUserReadStatus(@PathVariable UUID userId){
        return ResponseDTO.<List<ReadStatus>>builder()
                .code(HttpStatus.OK.value())
                .message("특정 사용자의 메시지 수신 정보를 조회")
                .data(readStatusService.findAllByUserId(userId))
                .build();
    }

}

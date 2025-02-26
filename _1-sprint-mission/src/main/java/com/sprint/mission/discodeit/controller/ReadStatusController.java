package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    //메시지 수신 정보 생성
    @RequestMapping(path = "create")
    public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateDto readStatusDto){
        ReadStatus createdReadStatus = readStatusService.create(readStatusDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReadStatus);
    }
    //메시지 수신 정보 수정
    @RequestMapping(path = "update")
    public ResponseEntity<ReadStatus> update(@RequestParam("readStatusId")UUID readStatusId,@RequestBody ReadStatusUpdateDto readStatusUpdateDto){
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
    }
    //미시지 수신 정보 목록 조회
    @RequestMapping(path = "findAllByUserId")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId){
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(readStatuses);
    }
}

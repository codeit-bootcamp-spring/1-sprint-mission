package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusFindDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/readstatus")
public class ReadStatusController {
  private final ReadStatusService readStatusService;
  public ReadStatusController(ReadStatusService readStatusService) {
    this.readStatusService = readStatusService;
  }

  // 특정 채널의 메시지 수신 정보 생성
  @RequestMapping(path="create")
  public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateDTO readStatusCreateDTO) {
    ReadStatus readStatus = readStatusService.create(readStatusCreateDTO);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatus);

  }

  @RequestMapping(path = "update")
  public ResponseEntity<ReadStatus> update(@RequestParam("readStatusId")
      @RequestBody ReadStatusUpdateDTO readStatusUpdateDTO) {
    ReadStatus updatedReadStatus = readStatusService.update( readStatusUpdateDTO);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedReadStatus);
  }

  @RequestMapping(path = "findAllByUserId")
  public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }


  }

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  // TODO: URI 수정
  @RequestMapping(value = "/channel/user/read-status", method = RequestMethod.POST)
  public ResponseEntity<ReadStatus> createReadStatus(@RequestBody CreateReadStatusDto dto){
    ReadStatus status = readStatusService.create(dto, false);
    return ResponseEntity.ok(status);
  }

  @RequestMapping(value = "/channel/user/read-status", method = RequestMethod.PUT)
  public ResponseEntity<ReadStatus> updateReadStatus(@RequestBody CreateReadStatusDto dto){
    ReadStatus status = readStatusService.update(dto);
    return ResponseEntity.ok(status);
  }

  @RequestMapping(value = "/channel/user/{userId}/read-status", method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatus>> getUserReadStatus(@PathVariable String userId){
    List<ReadStatus> status = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(status);
  }
}

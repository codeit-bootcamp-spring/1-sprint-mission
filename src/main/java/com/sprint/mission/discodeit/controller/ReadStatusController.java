package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReadStatusController {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @PostMapping("/read-statuses")
  public ResponseEntity<ReadStatusResponseDto> createReadStatus(@RequestBody CreateReadStatusDto dto){
    ReadStatusResponseDto status = readStatusMapper.toReadStatusResponseDto(readStatusService.create(dto, false));
    return ResponseEntity.ok(status);
  }

  @PatchMapping("/read-statuses/{id}")  // 이거 해야함
  public ResponseEntity<ReadStatusResponseDto> updateReadStatus(@PathVariable String id, @RequestBody UpdateReadStatusDto dto){
    ReadStatus status = readStatusService.updateById(dto, id);
    return ResponseEntity.ok(readStatusMapper.toReadStatusResponseDto(status));
  }

  @GetMapping("/readStatuses")
  public ResponseEntity<List<ReadStatusResponseDto>> getUserReadStatus(@RequestParam String userId){
    List<ReadStatusResponseDto> status = readStatusService.findAllByUserId(userId).stream().map(readStatusMapper::toReadStatusResponseDto).toList();
    return ResponseEntity.ok(status);
  }
}

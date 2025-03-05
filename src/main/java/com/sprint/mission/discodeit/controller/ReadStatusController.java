package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
@Tag(name = "ReadStatus", description = "메세지 읽음 상태 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  //개별 조회
//  @GetMapping("/{readStatusId}")
//  public ReadStatusResponseDto getReadStatus(@PathVariable String readStatusId) {
//    return readStatusService.findById(readStatusId);
//  }

  //개별 생성
  @PostMapping
  public ResponseEntity<ReadStatusResponseDto> createReadStatus(
      @RequestBody CreateReadStatusDto createReadStatusDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusService.create(createReadStatusDto));
  }

  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusResponseDto> updateReadStatus(@PathVariable String readStatusId,
      @RequestBody UpdateReadStatusDto updateReadStatusDto) {
    ReadStatusResponseDto updatedReadStatusDto = readStatusService.update(readStatusId,
        updateReadStatusDto);
    return ResponseEntity.ok().body(updatedReadStatusDto);
  }

  //특정 사용자의 메세지 수신 정보 조회
  @GetMapping
  public List<ReadStatusResponseDto> getReadStatusByUserId(@RequestParam String userId) {
    return readStatusService.findAllByUserId(userId);
  }

  //특정 채널의 메세지 수신 정보 생성
  @PostMapping("/{channelId}")
  public ResponseEntity<List<ReadStatusResponseDto>> createReadStatusByChannelId(
      @PathVariable String channelId) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusService.createByChannelId(channelId));
  }

  //특정 채널의 메세지 수신 정보 수정
  @PatchMapping
  public ResponseEntity<List<ReadStatusResponseDto>> updateChannelReadStatus(
      @RequestParam String channelId,
      @RequestBody UpdateReadStatusDto updateReadStatusDto) {
    return ResponseEntity.ok(readStatusService.updateByChannelId(channelId, updateReadStatusDto));
  }
}

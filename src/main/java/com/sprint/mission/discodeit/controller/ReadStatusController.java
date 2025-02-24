package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  //개별 조회
  @GetMapping("/{readStatusId}")
  public ReadStatusResponseDto getReadStatus(@PathVariable String readStatusId) {
    return readStatusService.findById(readStatusId);
  }

  //특정 사용자의 메세지 수신 정보 조회
  @GetMapping("/users/{userId}")
  public List<ReadStatusResponseDto> getReadStatusByUserId(@PathVariable String userId) {
    return readStatusService.findAllByUserId(userId);
  }

  //특정 채널의 메세지 수신 정보 생성
  @PostMapping("/{channelId}")
  public List<ReadStatusResponseDto> createReadStatusByChannelId(@PathVariable String channelId) {
    return readStatusService.createByChannelId(channelId);
  }

  //특정 채널의 메세지 수신 정보 수정
  @PutMapping("/{channelId}")
  public List<ReadStatusResponseDto> updateChannelReadStatus(@PathVariable String channelId,
      @RequestBody UpdateReadStatusDto updateReadStatusDto) {
    return readStatusService.updateByChannelId(channelId, updateReadStatusDto);
  }
}

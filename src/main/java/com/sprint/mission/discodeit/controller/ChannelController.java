package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;
  private final ChannelMapper channelMapper;

  //TODO: 반환을 DTO로 고치기.
  // 공개 채널 생성
  @PostMapping
  public ResponseEntity<ChannelDto> createPublicChannel(
      @RequestBody ChannelCreateDTO channelCreateDTO) {
    return ResponseEntity.ok(channelService.createPublicChannel(channelCreateDTO));
  }

  // 비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody PrivateChannelCreateDTO channelCreateDTO) {
    return ResponseEntity.ok(channelService.createPrivateChannel(channelCreateDTO));
  }

  // 공개 채널 정보 수정
  @PatchMapping("/{id}")
  public ResponseEntity<String> updateChannel(@PathVariable("id") UUID id,
      @RequestBody ChannelUpdateDTO channelUpdateDTO) {
    channelService.update(channelUpdateDTO);
    return ResponseEntity.status(HttpStatus.OK).body("Channel updated");
  }

  // 채널 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteChannel(@PathVariable("id") UUID id) {
    channelService.deleteChannel(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Channel deleted");
  }

  // 특정 사용자가 볼 수 있는 모든 채널 목록 조회
  @GetMapping("/{userId}")
  public ResponseEntity<List<ChannelDto>> findAllByUserId(@PathVariable("userId") UUID userId) {
    List<ChannelDto> channelDtoList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelDtoList);
  }

  // 모든 채널 목록 조회
  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll() {
    List<ChannelDto> channelDtoList = channelService.findAllDTO();
    return ResponseEntity.ok(channelDtoList);
  }
}
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  // 공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> createPublicChannel(
      @RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
    ChannelDto channelDto = channelService.createPublicChannel(publicChannelCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
  }

  // 비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
    ChannelDto channelDto = channelService.createPrivateChannel(privateChannelCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
  }

  // 채널 정보 수정
  @PutMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updateChannel(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
    ChannelDto channelDto = channelService.update(channelId, publicChannelUpdateRequest);
    return ResponseEntity.ok(channelDto);
  }

  // 채널 삭제
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  // 특정 사용자가 접근 가능한 채널 목록 조회
  @GetMapping("/users/{userId}")
  public ResponseEntity<List<ChannelDto>> getChannelsOfUser(@PathVariable UUID userId) {
    List<ChannelDto> channelDtoList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelDtoList);
  }
}

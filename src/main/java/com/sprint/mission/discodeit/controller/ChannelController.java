package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  //  공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<Channel> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPublicChannel(request));
  }

  //  비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPrivateChannel(request));
  }

  //  공개 채널 정보 수정
  @PutMapping("/{channelId}")
  public ResponseEntity<Channel> updateChannel(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    return ResponseEntity.ok(channelService.update(channelId, request));
  }

  //  채널 삭제
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  //  특정 사용자가 볼 수 있는 모든 채널 목록 조회
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ChannelDto>> getChannelsByUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId));
  }

}

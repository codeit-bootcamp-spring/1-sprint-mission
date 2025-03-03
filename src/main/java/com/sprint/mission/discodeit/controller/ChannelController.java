package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  // 공개 채널 생성
  @Override
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> createPublicChannel(
      @RequestBody PublicChannelCreateRequest request) {
    ChannelDto channelDto = channelService.createPublicChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
  }

  // 비공개 채널 생성
  @Override
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest request) {
    ChannelDto channelDto = channelService.createPrivateChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
  }

  // 채널 정보 수정
  @Override
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updatePublicChannel(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    ChannelDto channelDto = channelService.updatePublicChannel(channelId, request);
    return ResponseEntity.ok(channelDto);
  }

  // 채널 삭제
  @Override
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  // 특정 사용자가 접근 가능한 채널 목록 조회
  @Override
  @GetMapping
  public ResponseEntity<List<ChannelDto>> getChannelsOfUser(@RequestParam UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }
}

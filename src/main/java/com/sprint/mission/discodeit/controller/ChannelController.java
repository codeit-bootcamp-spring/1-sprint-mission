package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Tag(name = "Channel", description = "채널 API")
public class ChannelController {

  private final ChannelService channelService;

  //공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<ChannelResponseDto> creatPublicChannel(
      @RequestBody CreatePublicChannelDto createPublicChannelDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(channelService.create(
        createPublicChannelDto));
  }

  //비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<ChannelResponseDto> creatPrivateChannel(
      @RequestBody CreatePrivateChannelDTo createPrivateChannelDTo) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.create(createPrivateChannelDTo));

  }

  //공개 채널 정보 수정
  @PatchMapping("/{channelId}")
  public ChannelResponseDto updatePublicChannel(@PathVariable String channelId,
      @RequestBody UpdateChannelDto updateChannelDto) {
    return channelService.updateChannel(channelId, updateChannelDto);
  }

  //채널 삭제
  @DeleteMapping("/{channelId}")
  public ResponseEntity<String> deleteChannel(@PathVariable String channelId) {
    if (channelService.delete(channelId)) {
      return ResponseEntity.ok("Channel deleted successfully");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  //특정 사용자의 모든 채널 목록 조회
  //필터, 검색, 페이징에 해당하므로 쿼리파라미터로 수정
  @GetMapping
  public ResponseEntity<List<ChannelResponseDto>> getChannel(@RequestParam String userId) {
    return ResponseEntity.ok().body(channelService.findAllByUserId(userId));
  }

  //특정 채널의 모든 메세지 조회
  @GetMapping("/{channelId}/messages")
  public ResponseEntity<List<MessageResponseDto>> getAllMessages(@PathVariable String channelId) {
    return ResponseEntity.ok().body(channelService.findAllMessagesByChannelId(channelId));
  }
}

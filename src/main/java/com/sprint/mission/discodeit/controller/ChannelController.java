package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
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
  public ChannelResponseDto creatPublicChannel(@RequestBody CreateChannelDto createChannelDto) {
    return channelService.create(createChannelDto);
  }

  //비공개 채널 생성
  @PostMapping("/private")
  public ChannelResponseDto creatPrivateChannel(
      @RequestBody CreatePrivateChannelDTo createPrivateChannelDTo) {
    return channelService.create(createPrivateChannelDTo);
  }

  //공개 채널 정보 수정
  @PutMapping("/{channelId}")
  public ChannelResponseDto updatePublicChannel(@PathVariable String channelId,
      @RequestBody UpdateChannelDto updateChannelDto) {
    return channelService.updateChannel(channelId, updateChannelDto);
  }

  //채널 삭제
  @DeleteMapping("/{channelId}")
  public String deletePublicChannel(@PathVariable String channelId) {
    if (channelService.delete(channelId)) {
      return "Channel deleted successfully";
    }
    return "Channel deletion failed";
  }

  //특정 사용자의 모든 채널 목록 조회
  @GetMapping("/users/{userId}")
  public List<ChannelResponseDto> getPublicChannel(@PathVariable String userId) {
    return channelService.findAllByUserId(userId);
  }
}

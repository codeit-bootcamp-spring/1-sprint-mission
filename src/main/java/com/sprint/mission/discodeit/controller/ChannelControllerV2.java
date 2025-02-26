package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateResponse;
import com.sprint.mission.discodeit.entity.channel.find.ChannelFindOfUserResponse;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelControllerV2 {

  private final ChannelService channelService;

  @PostMapping("/public")
  public PublicChannelCreateResponse createPublicChannel(
      @RequestBody @Validated PublicChannelCreateRequest request) {

    return channelService.createPublicChannel(request);
  }

  /**
   * 채널에 참여하는 유저의 uuid를 받아 user별 readstatus 정보 생성 근데 name과 description 정보는 빼라고함
   */
  @PostMapping("/private")
  public PrivateChannelCreateResponse createPrivateChannel(
      @RequestBody @Validated PrivateChannelCreateRequest request) {

    return channelService.createPrivateChannel(request);
  }

  /**
   * user가 참여중인 channel 목록 조회
   */
  @GetMapping
  public List<ChannelFindOfUserResponse> findAllChannelsByUser(
      @RequestParam("userId") UUID userId) {
    return channelService.findAllChannelsFindByUserId(userId);
  }

  /**
   * 채널 수정
   */
  @PatchMapping("/{channelId}")
  public ChannelModifyResponse updateChannel(@PathVariable("channelId") UUID channelId,
      @Validated @RequestBody ChannelModifyRequest request) {

    return channelService.modifyChannel(channelId, request);
  }

  /**
   * 채널 삭제
   */
  @DeleteMapping("/{channelId}")
  public UUID deleteChannel(@PathVariable("channelId") UUID channelId) {
    return channelService.removeChannelById(channelId);
  }
}

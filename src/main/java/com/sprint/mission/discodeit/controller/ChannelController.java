package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Channel Controller")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  /**
   * public 채널 생성
   */
  @Operation(summary = "공개 채널 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/public")
  public ChannelDto createPublicChannel(
      @RequestBody @Validated PublicChannelCreateRequest request) {

    return channelService.createPublic(request);
  }

  /**
   * private 채널 생성
   * <p>
   * 채널에 참여하는 유저의 uuid를 받아 user별 readstatus 정보 생성 근데 name과 description 정보는 빼라고함
   */
  @Operation(summary = "private 채널 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/private")
  public ChannelDto createPrivateChannel(
      @RequestBody @Validated PrivateChannelCreateRequest request) {

    return channelService.createPrivate(request);
  }

  /**
   * 채널 삭제
   */
  @Operation(summary = "채널 삭제")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{channelId}")
  public String deleteChannel(@PathVariable("channelId") UUID channelId) {
    channelService.remove(channelId);

    return "";
  }

  /**
   * 채널 수정
   */
  @Operation(summary = "채널 정보 수정")
  @PatchMapping("/{channelId}")
  public ChannelDto updateChannel(@PathVariable("channelId") UUID channelId,
      @Validated @RequestBody ChannelModifyRequest request) {

    return channelService.update(channelId, request);
  }

  /**
   * user가 참여중인 channel 목록 조회
   */
  @Operation(summary = "유저가 참여 중인 채널 목록 조회")
  @GetMapping
  public List<ChannelDto> findAllChannelsByUser(
      @RequestParam("userId") UUID userId) {
    return channelService.findAllChannelsByUserId(userId);
  }


}

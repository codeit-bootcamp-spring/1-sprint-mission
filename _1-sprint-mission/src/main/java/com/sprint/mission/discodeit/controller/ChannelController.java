package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  //공개 채널 생성
  @PostMapping("/public")
  @Override
  public ResponseEntity<ChannelDto> create(
      @RequestBody PublicChannelCreateRequest publicChannelDto) {
    ChannelDto publicChannel = channelService.create(publicChannelDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
  }

  //비공개 채널 생성
  @PostMapping("/private")
  @Override
  public ResponseEntity<ChannelDto> create(
      @RequestBody PrivateChannelCreateRequest privateChannelDto) {
    ChannelDto privateChannel = channelService.create(privateChannelDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
  }

  //채널 목록 조회
  @GetMapping
  @Override
  public ResponseEntity<List<ChannelDto>> findAll(
      @RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(channels);
  }

  //공개 채널 정보 수정
  @PatchMapping("/{channelId}")
  @Override
  public ResponseEntity<ChannelDto> update(
      @PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
    ChannelDto updatedChannel = channelService.update(channelId, publicChannelUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
  }

  //채널 삭제
  @DeleteMapping("/{channelId}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.deleteById(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}

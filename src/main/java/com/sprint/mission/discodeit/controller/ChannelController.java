package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping(value = "/public")
  public ResponseEntity<ChannelDto> create(@Valid @RequestBody PublicChannelCreateRequest request) {
    log.info("공개 채널 생성 요청: {}", request);
    ChannelDto createChannel = channelService.create(request);
    log.debug("공개 채널 생성 응답: {}", createChannel);
    return ResponseEntity.status(HttpStatus.CREATED).body(createChannel);
  }

  @PostMapping(value = "/private")
  public ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request) {
    log.info("비공개 채널 생성 요청: {}", request);
    ChannelDto createChannel = channelService.create(request);
    log.debug("비공개 채널 생성 응답: {}", createChannel);
    return ResponseEntity.status(HttpStatus.CREATED).body(createChannel);
  }

  @PatchMapping(value = "/{channelId}")
  public ResponseEntity<ChannelDto> updateChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    log.info("채널 수정 요청: id={}, request={}", channelId, request);
    ChannelDto updateChannel = channelService.update(channelId, request);
    log.debug("채널 수정 응답: {}", updateChannel);
    return ResponseEntity.status(HttpStatus.OK).body(updateChannel);
  }

  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestParam UUID adminId) {
    log.info("채널 삭제 요청: id={}", channelId);
    channelService.delete(channelId, adminId);
    log.debug("채널 삭제 완료");
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(value = "/{userId}")
  public ResponseEntity<List<ChannelDto>> getUserChannels(@PathVariable("userId") UUID userId) {
    log.info("사용자별 채널 목록 조회 요청: userId={}", userId);
    List<ChannelDto> channelList = channelService.findAllByUserId(userId);
    log.debug("사용자별 채널 목록 조회 응답: count={}", channelList.size());
    return ResponseEntity.status(HttpStatus.OK).body(channelList);
  }

  @GetMapping(value = "")
  public ResponseEntity<List<ChannelDto>> getPublicChannels() {
    log.info("공개 채널 목록 조회 요청");
    List<ChannelDto> channelList = channelService.findPublicAll();
    log.debug("공개 채널 목록 조회 응답: count={}", channelList.size());
    return ResponseEntity.status(HttpStatus.OK).body(channelList);
  }

}

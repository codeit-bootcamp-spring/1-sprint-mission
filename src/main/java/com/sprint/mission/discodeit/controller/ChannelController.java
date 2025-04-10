package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Tag(name = "Channel", description = "채널 API")
public class ChannelController {

  private final ChannelService channelService;

  //공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<ChannelDto> creatPublicChannel(
      @Valid @RequestBody CreatePublicChannelDto createPublicChannelDto) {
    log.info("Public 채널 생성 요청: newName = {}", createPublicChannelDto.name());
    try {
      ChannelDto channelDto = channelService.create(createPublicChannelDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  //비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<ChannelDto> creatPrivateChannel(
      @Valid @RequestBody CreatePrivateChannelDTo createPrivateChannelDTo) {
    log.info("Private 채널 생성 요청");
    try {
      ChannelDto channelDto = channelService.create(createPrivateChannelDTo);
      return ResponseEntity.status(HttpStatus.CREATED).body(channelDto);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  //공개 채널 정보 수정
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelDto> updatePublicChannel(@PathVariable String channelId,
      @Valid @RequestBody UpdateChannelDto updateChannelDto) {
    log.info("채널 수정 요청: channelId = {}", channelId);
    try {
      ChannelDto channelDto = channelService.updateChannel(channelId, updateChannelDto);
      return ResponseEntity.status(HttpStatus.OK).body(channelDto);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  //채널 삭제
  @DeleteMapping("/{channelId}")
  public ResponseEntity<String> deleteChannel(@PathVariable String channelId) {
    log.info("채널 삭제 요청: channelId = {}", channelId);
    try {
      channelService.delete(channelId);
      return ResponseEntity.ok("Channel deleted successfully");
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  //특정 사용자의 모든 채널 목록 조회
  //필터, 검색, 페이징에 해당하므로 쿼리파라미터로 수정
  @GetMapping
  public ResponseEntity<List<ChannelDto>> getChannel(@RequestParam String userId) {
    return ResponseEntity.ok().body(channelService.findAllByUserId(userId));
  }

  //특정 채널의 모든 메세지 조회
  @GetMapping("/{channelId}/messages")
  public ResponseEntity<List<MessageDto>> getAllMessages(@PathVariable String channelId) {
    return ResponseEntity.ok().body(channelService.findAllMessagesByChannelId(channelId));
  }
}

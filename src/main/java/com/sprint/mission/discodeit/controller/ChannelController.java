package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping(value = "/public")
  public ResponseEntity<ChannelDto> createPublicChannel(
      @Valid @RequestBody ChannelPublicRequest channelPublicRequest) {
    log.info("공개 채널 생성 요청(Request): publicChannelName={}", channelPublicRequest.name());

    log.info("공개 채널 생성 응답(Response): publicChannelName={}, HttpStatus={}",
        channelPublicRequest.name(),
        HttpStatus.CREATED);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPublicChannel(channelPublicRequest)); // 201
  }

  @PostMapping(value = "/private")
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody ChannelPrivateRequest channelPrivateRequest) {
    log.info("비공개 채널 생성 요청(Request)");

    log.info("비공개 채널 생성 응답(Response): HttpStatus={}", HttpStatus.CREATED);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPrivateChannel(channelPrivateRequest)); // 201
  }


  @PatchMapping(value = "/{channelId}")
  public ResponseEntity<ChannelDto> updatePublicChannel(@PathVariable("channelId") UUID channelId,
      @Valid @RequestBody ChannelUpdateRequest channelUpdateRequest) {
    log.info("채널 수정 요청(Request): nameChanged={}, descriptionChanged={}",
        channelUpdateRequest.newName() != null,
        channelUpdateRequest.newDescription() != null
    );

    log.info("채널 수정 응답(Response): HttpStatus={}", HttpStatus.OK);
    return ResponseEntity.ok(channelService.updateChannel(channelId, channelUpdateRequest));
  }

  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable("channelId") UUID channelId) {
    log.info("채널 삭제 요청(Request)");
    channelService.deleteChannelById(channelId);
    log.info("채널 삭제 응답(Response): HttpStatus={}", HttpStatus.NO_CONTENT);
    return ResponseEntity.noContent().build(); // 204
  }

  // /api/channels?userId=1
  @GetMapping
  public ResponseEntity<Collection<ChannelDto>> getChannelListByUserId(
      @RequestParam UUID userId) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId)); // 200
  }
}

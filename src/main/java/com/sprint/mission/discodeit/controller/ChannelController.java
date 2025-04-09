package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<ChannelDto> create(@RequestBody PublicChannelCreateRequest request) {
    ChannelDto createdChannel = channelService.createPublicChannel(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request) {
    ChannelDto createdChannel = channelService.createPrivateChannel(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @PatchMapping("/{channel-id}")
  public ResponseEntity<ChannelDto> update(@PathVariable("channel-id") UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    ChannelDto updatedChannel = channelService.update(channelId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedChannel);
  }

  @DeleteMapping("/{channel-id}")
  public ResponseEntity<Void> delete(@PathVariable("channel-id") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }


  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("user-id") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }
}

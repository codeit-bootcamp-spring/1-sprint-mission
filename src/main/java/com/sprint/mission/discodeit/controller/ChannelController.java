package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping(value = "/public")
  public ResponseEntity<ChannelDto> create(@Valid @RequestBody PublicChannelCreateRequest request) {
    ChannelDto channelDTO = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channelDTO);
  }

  @PostMapping(value = "/private")
  public ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request) {
    ChannelDto channelDTO = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channelDTO);
  }

  @PatchMapping(value = "/{channelId}")
  public ResponseEntity<ChannelDto> updateChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    ChannelDto channelDTO = channelService.update(channelId, request);
    return ResponseEntity.status(HttpStatus.OK).body(channelDTO);
  }

  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestParam UUID adminId) {
    channelService.delete(channelId, adminId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(value = "/{userId}")
  public ResponseEntity<List<ChannelDto>> getUserChannels(@PathVariable("userId") UUID userId) {
    return ResponseEntity.status(HttpStatus.OK).body(channelService.findAllByUserId(userId));
  }

  @GetMapping(value = "")
  public ResponseEntity<List<ChannelDto>> getPublicChannels() {
    return ResponseEntity.status(HttpStatus.OK).body(channelService.findPublicAll());
  }

}

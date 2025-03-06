package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
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
  public ResponseEntity<ChannelDTO> create(@Valid @RequestBody PublicChannelCreateRequest request) {
    Channel channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ChannelDTO.fromEntity(channel));
  }

  @PostMapping(value = "/private")
  public ResponseEntity<ChannelDTO> create(
      @RequestBody PrivateChannelCreateRequest request) {
    Channel channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(ChannelDTO.fromEntity(channel));
  }

  @PutMapping(value = "/{channelId}")
  public ResponseEntity<ChannelDTO> updateChannel(
      @PathVariable("channelId") UUID channelId,
      @Valid @RequestBody PublicChannelUpdateRequest request) {
    Channel channel = channelService.update(channelId, request);
    return ResponseEntity.status(HttpStatus.OK).body(ChannelDTO.fromEntity(channel));
  }

  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestParam UUID adminId) {
    channelService.delete(channelId, adminId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(value = "/{userId}")
  public ResponseEntity<List<ChannelDTO>> getUserChannels(@PathVariable("userId") UUID userId) {
    return ResponseEntity.status(HttpStatus.OK).body(channelService.findAllByUserId(userId));
  }

  @GetMapping(value = "")
  public ResponseEntity<List<ChannelDTO>> getPublicChannels() {
    return ResponseEntity.status(HttpStatus.OK).body(channelService.findPublicAll());
  }

  @PostMapping(value = "/{channelId}/join")
  public ResponseEntity<Void> joinPrivateChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestParam UUID adminId,
      @RequestParam UUID userId) {
    channelService.joinPrivateChannel(channelId, adminId, userId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping(value = "/{channelId}/leave")
  public ResponseEntity<Void> leavePrivateChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestParam UUID userId) {
    channelService.leavePrivateChannel(channelId, userId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}

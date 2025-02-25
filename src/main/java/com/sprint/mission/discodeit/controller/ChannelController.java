package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<UUID> createPublicChannel(
      @RequestBody PublicChannelCreateRequest channelDto) {
    UUID id = channelService.createPublicChannel(channelDto);
    return ResponseEntity.ok(id);
  }

  @PostMapping("/private")
  public ResponseEntity<UUID> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest channelDto) {
    UUID id = channelService.createPrivateChannel(channelDto);
    return ResponseEntity.ok(id);
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> getChannels(@RequestParam("userId") String userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(UUID.fromString(userId));
    return ResponseEntity.ok(channels);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ChannelDto> getChannel(@PathVariable("id") UUID id) {
    ChannelDto channel = channelService.findById(id);
    return ResponseEntity.ok(channel);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateChannelName(@PathVariable("id") UUID id,
      @RequestParam("name") String name) {
    channelService.updateName(id, name);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/members")
  public ResponseEntity<PrivateChannelUpdateRequest> updateChannelMembers(
      @PathVariable("id") UUID id,
      @RequestBody List<UUID> memberIds) {
    PrivateChannelUpdateRequest channelDto = new PrivateChannelUpdateRequest(id, memberIds);
    channelService.updateMember(channelDto);
    return ResponseEntity.ok(channelDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removeChannel(@PathVariable("id") UUID id,
      @RequestParam("ownerId") UUID ownerId) {
    channelService.remove(id, ownerId);
    return ResponseEntity.ok().build();
  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdate;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<Channel> create(@RequestBody PublicChannelCreate publicChannelCreate) {
    Channel createdChannel = channelService.createChannelPublic(publicChannelCreate);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
  }

  @PostMapping("/private")
  public ResponseEntity<Channel> create(@RequestBody PrivateChannelCreate privateChannelCreate) {
    Channel createdChannel = channelService.createChannelPrivate(privateChannelCreate);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
  }

  @PutMapping("/{channelId}")
  public ResponseEntity<Channel> update(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdate publicChannelUpdate) {
    Channel updatedChannel = channelService.update(channelId, publicChannelUpdate);
    return ResponseEntity.ok(updatedChannel);
  }

  @DeleteMapping
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserID(userId);
    return ResponseEntity.ok(channels);
  }
}

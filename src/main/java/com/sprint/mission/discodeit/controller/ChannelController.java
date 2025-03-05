package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.ChannelApiDocs;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApiDocs {

  private final ChannelService channelService;

  @PostMapping("/public")
  public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
  }

  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivate(@RequestBody PrivateChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> update(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel updatedChannel = channelService.update(channelId, request);
    return ResponseEntity.ok(updatedChannel);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ChannelDto>> findAll(@PathVariable UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }

  @GetMapping()
  public ResponseEntity<List<ChannelDto>> findAll() {
    return ResponseEntity.ok(channelService.findAll());
  }

}

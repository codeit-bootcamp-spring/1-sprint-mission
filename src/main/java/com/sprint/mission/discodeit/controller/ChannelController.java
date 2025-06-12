package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @PostMapping("public")
  public ResponseEntity<ChannelDto> createPublic(
      @Valid @RequestBody ChannelCreatePublicDTO request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channelService.create(request));
  }

  @PostMapping("private")
  public ResponseEntity<ChannelDto> createPrivate(
      @RequestBody ChannelCreatePrivateDTO request) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channelService.create(request));
  }

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @PatchMapping("{channelId}")
  public ResponseEntity<ChannelDto> update(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateDTO request) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channelService.update(channelId, request));
  }

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @DeleteMapping("{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }


  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channelService.findAllByUserId(userId));
  }
}

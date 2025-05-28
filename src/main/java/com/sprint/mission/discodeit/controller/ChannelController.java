package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelControllerDocs {

  private final ChannelService channelService;

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @PostMapping("/public")
  public ResponseEntity<ChannelResponseDto> createPublicChannel(
      @Valid @RequestBody PublicChannelRequestDto channelReqDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPublicChannel(channelReqDto));
  }

  @PostMapping("/private")
  public ResponseEntity<ChannelResponseDto> createPrivateChannel(
      @Valid @RequestBody PrivateChannelRequestDto channelReqDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPrivateChannel(channelReqDto));
  }

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @PutMapping("/{channelId}")
  public ResponseEntity<ChannelResponseDto> updatePublicChannel(
      @PathVariable UUID channelId, @RequestBody ChannelUpdateDto updateDto) {
    return ResponseEntity.ok(channelService.update(channelId, updateDto));
  }

  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<ChannelResponseDto> deleteChannel(@PathVariable UUID channelId) {
    return ResponseEntity.ok(channelService.delete(channelId));
  }

  @GetMapping
  public ResponseEntity<List<ChannelResponseDto>> getChannels(@RequestParam UUID userId) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId));
  }
}

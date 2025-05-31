package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.request.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelControllerDocs {

  private final ChannelService channelService;

  @PostMapping("/public")
  @Override
  public ResponseEntity<ChannelDto> createPublicChannel(
      @RequestBody @Valid PublicChannelRequest publicChannelRequest
  ) {
    log.debug("POST /api/channels/public");
    ChannelDto publicChannel = channelService.createPublicChannel(publicChannelRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
  }

  @PostMapping("/private")
  @Override
  public ResponseEntity<ChannelDto> createPrivateChannel(
      @RequestBody @Valid PrivateChannelRequest privateChannelRequest
  ) {
    log.debug("POST /api/channels/private");
    ChannelDto privateChannel = channelService.
        createPrivateChannel(privateChannelRequest.participantIds());
    return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
  }

  @GetMapping
  @Override
  public ResponseEntity<List<ChannelDto>> getChannels(@RequestParam UUID userId) {
    log.debug("GET /api/channels");
    return ResponseEntity.ok(channelService.readAllByUserId(userId));
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<ChannelDto> updatePublicChannel(
      @PathVariable UUID id,
      @RequestBody @Valid PublicChannelUpdateRequest updateRequest
  ) {
    log.debug("PATCH /api/channels/{}", id);
    ChannelDto channel = channelService.updateChannel(id, updateRequest);
    return ResponseEntity.ok().body(channel);
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
    log.debug("DELETE /api/channels/{}", id);
    channelService.deleteChannel(id);
    return ResponseEntity.noContent().build();
  }
}

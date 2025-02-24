package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.ChannelApiDocs;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.service.facade.channel.ChannelMasterFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApiDocs {

  private final ChannelMasterFacade channelMasterFacade;

  @Override
  @PostMapping("/private")
  public ResponseEntity<PrivateChannelResponseDto> createPrivateChannel(@RequestBody CreatePrivateChannelDto channelDto) {
    PrivateChannelResponseDto channel = channelMasterFacade.createPrivateChannel(channelDto);
    return ResponseEntity.status(201).body(channel);
  }


  @Override
  @PostMapping("/public")
  public ResponseEntity<PublicChannelResponseDto> createPublicChannel(@RequestBody CreateChannelDto channelDto) {
    PublicChannelResponseDto channel = channelMasterFacade.createPublicChannel(channelDto);
    return ResponseEntity.status(201).body(channel);
  }

  @Override
  @PatchMapping("/{channelId}")
  public ResponseEntity<UpdateChannelResponseDto> updateChannel(@PathVariable String channelId, @RequestBody ChannelUpdateDto channelDto) {
    UpdateChannelResponseDto channel = channelMasterFacade.updateChannel(channelId, channelDto);
    return ResponseEntity.ok(channel);
  }


  @Override
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable String channelId) {
    channelMasterFacade.deleteChannel(channelId);
    return ResponseEntity.status(204).build();
  }


  @Override
  @GetMapping
  public ResponseEntity<List<FindChannelResponseDto>> findChannelVisibleToUser(@RequestParam String userId){
    List<FindChannelResponseDto> channels = channelMasterFacade.findAllChannelsByUserId(userId);
    return ResponseEntity.ok(channels);
  }
}

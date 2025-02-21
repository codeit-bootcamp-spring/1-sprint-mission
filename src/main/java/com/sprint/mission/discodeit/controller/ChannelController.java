package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
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
public class ChannelController {

  private final ChannelMasterFacade channelMasterFacade;

  @PostMapping("/private")
  public ResponseEntity<PrivateChannelResponseDto> createPrivateChannel(@RequestBody CreatePrivateChannelDto channelDto) {
    PrivateChannelResponseDto channel = channelMasterFacade.createPrivateChannel(channelDto);
    return ResponseEntity.status(201).body(channel);
  }


  @PostMapping("/public")
  public ResponseEntity<PublicChannelResponseDto> createPublicChannel(@RequestBody CreateChannelDto channelDto) {
    PublicChannelResponseDto channel = channelMasterFacade.createPublicChannel(channelDto);
    return ResponseEntity.status(201).body(channel);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<FindChannelResponseDto> updateChannel(@PathVariable String id, @RequestBody ChannelUpdateDto channelDto) {
    FindChannelResponseDto channel = channelMasterFacade.updateChannel(id, channelDto);
    return ResponseEntity.ok(channel);
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteChannel(@PathVariable String id) {
    channelMasterFacade.deleteChannel(id);
    return ResponseEntity.ok("successfully deleted");
  }


  @GetMapping("/users/{id}")
  public ResponseEntity<List<FindChannelResponseDto>> findChannelVisibleToUser(@PathVariable String id){
    List<FindChannelResponseDto> channels = channelMasterFacade.findAllChannelsByUserId(id);
    return ResponseEntity.ok(channels);
  }
}

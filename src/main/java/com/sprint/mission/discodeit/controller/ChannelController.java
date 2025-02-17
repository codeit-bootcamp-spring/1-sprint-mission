package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.service.facade.ChannelMasterFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/channel")
public class ChannelController {

  private final ChannelMasterFacade channelMasterFacade;

  @RequestMapping(value = "/create-private", method = RequestMethod.POST)
  public ResponseEntity<PrivateChannelResponseDto> createPrivateChannel(@RequestBody CreatePrivateChannelDto channelDto) {
    PrivateChannelResponseDto channel = channelMasterFacade.createPrivateChannel(channelDto);
    return ResponseEntity.ok(channel);
  }

  @RequestMapping(value = "/create-public", method = RequestMethod.POST)
  public ResponseEntity<PublicChannelResponseDto> createPublicChannel(@RequestBody CreateChannelDto channelDto) {
    PublicChannelResponseDto channel = channelMasterFacade.createPublicChannel(channelDto);
    return ResponseEntity.ok(channel);
  }

  @RequestMapping(value = "/{id}/update", method = RequestMethod.PATCH)
  public ResponseEntity<FindChannelResponseDto> updateChannel(@PathVariable String id, @RequestBody ChannelUpdateDto channelDto){
    FindChannelResponseDto channel = channelMasterFacade.updateChannel(id, channelDto);
    return ResponseEntity.ok(channel);
  }
}

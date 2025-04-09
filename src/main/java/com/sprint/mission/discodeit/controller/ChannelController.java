package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.ChannelApiDocs;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.service.facade.channel.ChannelMasterFacade;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApiDocs {

  private final ChannelMasterFacade channelMasterFacade;

  @Override
  @PostMapping("/private")
  public ResponseEntity<ChannelResponseDto> createPrivateChannel(
      @Valid @RequestBody CreatePrivateChannelDto channelDto) {
    log.debug("[CREATE PRIVATE CHANNEL REQUEST] : [IDS: {}]", channelDto.participantIds());

    ChannelResponseDto channel = channelMasterFacade.createPrivateChannel(channelDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }


  @Override
  @PostMapping("/public")
  public ResponseEntity<ChannelResponseDto> createPublicChannel(
      @Valid @RequestBody CreateChannelDto channelDto) {
    log.debug("[CREATE PUBLIC CHANNEL REQUEST] : [NAME: {}][DESCRIPTION: {}]", channelDto.name(),
        channelDto.description());
    ChannelResponseDto channel = channelMasterFacade.createPublicChannel(channelDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  @Override
  @PatchMapping("/{channelId}")
  public ResponseEntity<ChannelResponseDto> updateChannel(@PathVariable String channelId,
      @Valid @RequestBody ChannelUpdateDto channelDto) {
    log.debug("[UPDATE CHANNEL REQUEST] : [ID: {}]", channelId);
    ChannelResponseDto channel = channelMasterFacade.updateChannel(channelId, channelDto);
    log.debug("[CHANNEL UPDATE SUCCESSFUL] : [ID: {}]", channelId);
    return ResponseEntity.ok(channel);
  }


  @Override
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable String channelId) {
    log.debug("[DELETE CHANNEL REQUEST] : [ID : {}]", channelId);
    channelMasterFacade.deleteChannel(channelId);

    log.debug("[DELETED CHANNEL] : [ID : {}]", channelId);

    return ResponseEntity.noContent().build();
  }


  @Override
  @GetMapping
  public ResponseEntity<List<ChannelResponseDto>> findChannelVisibleToUser(
      @RequestParam String userId) {

    List<ChannelResponseDto> channels = channelMasterFacade.findAllChannelsByUserIdV3(userId);
    return ResponseEntity.ok(channels);
  }
}

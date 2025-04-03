package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @Override
  public ResponseEntity<ChannelResponse> createChannel(@RequestBody CreateChannelRequest request) {
    ChannelResponse response = null;
    try {
      response = channelService.createChannel(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @RequestBody CreatePrivateChannelRequest request
  ) {
    ChannelResponse response = channelService.createPrivateChannel(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<List<ChannelResponse>> getChannels() {
    List<ChannelResponse> channels = channelService.getChannelsResponse();
    return ResponseEntity.ok(channels);
  }

  @Override
  public ResponseEntity<List<ChannelResponse>> findAll_1(
      @Parameter(description = "검색할 userId") @RequestParam UUID userId
  ) {
    List<ChannelResponse> channels = channelService.getChannelsByUserId(userId);
    return ResponseEntity.ok(channels);
  }

  @Override
  public ResponseEntity<ChannelResponse> getChannel(
      @Parameter(description = "조회할 채널의 ID", required = true) @PathVariable UUID id
  ) {
    return ResponseEntity.ok(channelService.getChannelResponse(id));
  }

  @Override
  public ResponseEntity<ChannelResponse> updateChannel(
      @Parameter(description = "수정할 채널의 ID", required = true) @PathVariable UUID id,
      @RequestBody UpdateChannelRequest request
  ) {
    return ResponseEntity.ok(channelService.updateChannel(id, request));
  }

  @Override
  public ResponseEntity<Void> deleteChannel(
      @Parameter(description = "삭제할 채널의 ID", required = true) @PathVariable UUID id
  ) {
    channelService.deleteChannel(id);
    return ResponseEntity.noContent().build();
  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ChannelSwagger;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.dto.response.PublicChannelCreateResponse;
import com.sprint.mission.discodeit.dto.response.PublicChannelUpdateResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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


@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelSwagger {

  // implements ChannelSwagger
  private final ChannelService channelService;

  @PostMapping(value = "/public") //String name,String description
  public ResponseEntity<PublicChannelCreateResponse> createPublic(
      @RequestBody PublicChannelCreateRequest request
  ) {
    Channel channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(PublicChannelCreateResponse.from(channel));
  }

  @PostMapping(value = "/private")//List<UUID> participantIds
  public ResponseEntity<PrivateChannelCreateResponse> createPrivate(
      @RequestBody PrivateChannelCreateRequest request
  ) {
    Channel channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(PrivateChannelCreateResponse.from(channel));
  }

  //  @PatchMapping(value = "/public/{channelId}")
//  public ResponseEntity<PublicChannelUpdateResponse> update(
//      @PathVariable UUID channelId,
//      @RequestBody PublicChannelUpdateRequest request
//  ) {
//    Channel channel = channelService.update(channelId, request);
//    return ResponseEntity.status(HttpStatus.OK).body(PublicChannelUpdateResponse.from(channel));
//  }
  @PatchMapping(value = "/{channelId}")
  public ResponseEntity<PublicChannelUpdateResponse> update(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request
  ) {
    Channel channel = channelService.update(channelId, request);
    return ResponseEntity.status(HttpStatus.OK).body(PublicChannelUpdateResponse.from(channel));
  }

  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

//    @GetMapping("/user/{userId}")
//  public ResponseEntity<ChannelListDto> findChannelsByUserV0(
//      @PathVariable UUID userId
//  ) {
//    ChannelListDto channelListDto = channelService.findAllByUserId(userId);
//    return ResponseEntity.ok(channelListDto);
//  }


  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(
      @RequestParam UUID userId
  ) {
    List<ChannelDto> channelListDto = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelListDto);
  }
}
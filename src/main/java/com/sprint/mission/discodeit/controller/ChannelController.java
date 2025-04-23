package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdatePublicChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ChannelController implements ChannelApi {

  private static final Logger log = LoggerFactory.getLogger(ChannelController.class);
  private final ChannelService channelService;

  @Override
  @PostMapping(path = "public")
  public ResponseEntity<ChannelDto> create(
      @Valid @RequestBody CreatePublicChannelRequest request) {

    log.info("Public Channel 생성 요청 : {}", request);

    ChannelDto channelDto = channelService.create(request);

    log.debug("Public Channel 생성 응답 : {}", channelDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channelDto);
  }

  @Override
  @PostMapping(path = "private")
  public ResponseEntity<ChannelDto> create(
      @RequestBody CreatePrivateChannelRequest request) {

    log.info("Private Channel 생성 요청 : {}", request);

    ChannelDto channelDto = channelService.create(request);

    log.debug("Private Channel 생성 응답 : {}", channelDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channelDto);
  }

  @Override
  @PatchMapping(path = "{channelId}")
  public ResponseEntity<ChannelDto> update(
      @PathVariable("channelId") UUID channelId,
      @Valid @RequestBody UpdatePublicChannelRequest request) {

    log.info("Public Channel 수정 요청 : channelId={}, request={}", channelId, request);

    ChannelDto channelDto = channelService.update(channelId, request);

    log.debug("Public Channel 수정 응답 : {}", channelDto);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channelDto);
  }

  @Override
  @DeleteMapping(path = "{channelId}")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {

    log.info("Public Channel 삭제 요청 : channelId={}", channelId);

    channelService.delete(channelId);

    log.debug("Public Channel 삭제 성공 : channelId={}", channelId);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {

    List<ChannelDto> channelDtos = channelService.findAllByUserId(userId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channelDtos);
  }
}

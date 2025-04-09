package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.basic.ChannelService;
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

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @PostMapping(path = "public")
  public ResponseEntity<ChannelDto> create(@RequestBody @Valid PublicChannelCreateRequest request) {
    log.info("Public 채널 생성 요청 수신 - name: {}, description: {}", request.name(),
        request.description());
    try {
      ChannelDto createdChannel = channelService.create(request);
      log.info("Public 채널 생성 성공 - name: {}, description: {}", request.name(),
          request.description());
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(createdChannel);
    } catch (Exception e) {
      log.error("Public 채널 생성 실패 - name: {}, description: {}, 원인: {}", request.name(),
          request.description(), e.getMessage());
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }

  }

  @PostMapping(path = "private")
  public ResponseEntity<ChannelDto> create(
      @RequestBody @Valid PrivateChannelCreateRequest request) {
    ChannelDto createdChannel = channelService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @PatchMapping(path = "{channelId}")
  public ResponseEntity<ChannelDto> update(@PathVariable("channelId") UUID channelId,
      @RequestBody @Valid PublicChannelUpdateRequest request) {
    log.info("채널 업데이트 요청 - channelId: {}, request: {}", channelId, request);
    try {
      ChannelDto updatedChannel = channelService.update(channelId, request);
      log.info("채널 업데이트 성공 - channelId: {}", updatedChannel.id());
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(updatedChannel);
    } catch (Exception e) {
      log.error("채널 업데이트 실패 - channelId: {}, 원인: {}",
          channelId, e.getMessage(), e);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  @DeleteMapping(path = "{channelId}")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    log.info("채널 삭제 요청 - channelId: {}", channelId);

    try {
      channelService.delete(channelId);
      log.info("채널 삭제 성공 - channelId: {}", channelId);
      return ResponseEntity
          .status(HttpStatus.NO_CONTENT)
          .build();
    } catch (Exception e) {
      log.error("채널 삭제 실패 - channelId: {}, 원인: {}",
          channelId, e.getMessage(), e);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .build();
    }
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }
}

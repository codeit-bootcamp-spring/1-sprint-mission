package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @PostMapping(path = "public")
  public ResponseEntity<ChannelDto> create(@RequestBody @Valid PublicChannelCreateRequest request) {
    log.info("공개 채널 생성 요청: {}", request);
    ChannelDto createdChannel = channelService.create(request);
    log.debug("공개 채널 생성 응답: {}", createdChannel);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @PostMapping(path = "private")
  public ResponseEntity<ChannelDto> create(@RequestBody @Valid PrivateChannelCreateRequest request) {
    log.info("비공개 채널 생성 요청: {}", request);
    ChannelDto createdChannel = channelService.create(request);
    log.debug("비공개 채널 생성 응답: {}", createdChannel);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  @PatchMapping(path = "{channelId}")
  public ResponseEntity<ChannelDto> update(
      @PathVariable("channelId") UUID channelId,
      @RequestBody @Valid PublicChannelUpdateRequest request) {
    log.info("채널 수정 요청: id={}, request={}", channelId, request);
    ChannelDto updatedChannel = channelService.update(channelId, request);
    log.debug("채널 수정 응답: {}", updatedChannel);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedChannel);
  }

  @DeleteMapping(path = "{channelId}")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    log.info("채널 삭제 요청: id={}", channelId);
    channelService.delete(channelId);
    log.debug("채널 삭제 완료");
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    log.info("사용자별 채널 목록 조회 요청: userId={}", userId);
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    log.debug("사용자별 채널 목록 조회 응답: count={}", channels.size());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
@Slf4j
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  //  공개 채널 생성
  @PostMapping(path = "public")
  public ResponseEntity<ChannelDto> create(
     @Valid @RequestBody PublicChannelCreateRequest request) {
    log.info("공개 채널 생성 요청: name={}, description={}", request.name(), request.description());

    ChannelDto createdChannel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  //  비공개 채널 생성
  @PostMapping(path = "private")
  public ResponseEntity<ChannelDto> create(
     @Valid @RequestBody PrivateChannelCreateRequest request) {
    log.info("비공개 채널 생성 요청: 참여자 수={}", request.participantIds().size());

    ChannelDto createdChannel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  //  공개 채널 정보 수정
  @PatchMapping(path = "{channelId}")
  public ResponseEntity<ChannelDto> update(@PathVariable("channelId") UUID channelId,
     @Valid @RequestBody PublicChannelUpdateRequest request) {
    log.info("채널 수정 요청: channelId={}, newName={}, newDescription={}",
            channelId, request.newName(), request.newDescription());

    ChannelDto updatedChannel = channelService.update(channelId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedChannel);
  }

  //  채널 삭제
  @DeleteMapping(path = "{channelId}")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    log.warn("채널 삭제 요청: channelId={}", channelId);

    channelService.delete(channelId);
    return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
  }

  //  특정 사용자가 볼 수 있는 모든 채널 목록 조회
  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }

}

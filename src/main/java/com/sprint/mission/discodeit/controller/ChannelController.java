package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping(value = "/public")
  public ResponseEntity<Channel> createPublicChannel(
      @RequestBody ChannelPublicRequest channelPublicRequest) {

    /* 스프린트 미션 5 심화 조건 중 API 스펙을 준수
    ChannelPublicResponse channelPublicResponse = channelService.createPublicChannel(
        channelPublicRequest);
     */

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPublicChannel(channelPublicRequest)); // 201
  }

  @PostMapping(value = "/private")
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody ChannelPrivateRequest channelPrivateRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channelService.createPrivateChannel(channelPrivateRequest)); // 201
  }


  // 스프린트 미션 5 심화 조건 중 API 스펙을 준수를 위해 변경했지만,
  // /public/{channelId}, /private/{channelId} 처럼 구분하는 건 어떨까요?
  // 제가 느끼기에는 확장성은 조금 부족할지라도, 엔드포인트를 통해 public 채널인지 private 채널인지 확실히 구분할 수 있어 보여 좋아보이는데
  // 위와 같은 엔드 포인트를 사용했을 때의 단점도 궁금합니다!(public, prviate 뿐만 아니라 type 더 추가되면 더 복잡해진다거나)
  // 무엇보다 Public 채널을 업데이트하는 건데 엔드 포인트에 public이 붙지 않는 것도 고민되는 부분입니다.
  @PatchMapping(value = "/{channelId}")
  public ResponseEntity<Channel> updatePublicChannel(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest channelUpdateRequest) {
    return ResponseEntity.ok(channelService.updateChannel(channelId, channelUpdateRequest));
  }

  // 궁금한게, Public 과 Private 채널을 만들 땐 /public, /private 엔드포인트로 들어오는데 삭제할 때는 id만 해놔도 되는지
  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable UUID channelId) {
    channelService.deleteChannelById(channelId);
    return ResponseEntity.noContent().build(); // 204
  }

  @GetMapping
  public ResponseEntity<Collection<ChannelFindAllResponse>> getChannelListByUserId(
      @RequestParam UUID userId) {
    return ResponseEntity.ok(channelService.findAllByUserId(userId)); // 200
  }
}

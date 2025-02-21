package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateDTO;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//네이밍, HTTP 메서드, 상태 코드, 응답 구조 일관성
@RestController
@RequestMapping("/api/channels")
@Validated
public class ChannelController {

  private final ChannelService channelService;

  public ChannelController(ChannelService channelService) {
    this.channelService = channelService;
  }

  //공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<ChannelResponseDTO> createChannel(
      @RequestBody PublicChannelCreateDTO createDTO) {
    ChannelResponseDTO publicChannel = channelService.createPublicChannel(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
  }

  //비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<ChannelResponseDTO> createPrivateChannel(
      @RequestBody PrivateChannelCreateDTO createDTO) {
    ChannelResponseDTO privateChannel = channelService.createPrivateChannel(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
  }

  //공개 채널의 정보를 수정
  @PatchMapping("/public")
  public ResponseEntity<ChannelResponseDTO> updatePublicChannel(
      @RequestBody ChannelUpdateDTO updateDTO) {
    ChannelResponseDTO update = channelService.update(updateDTO);
    return ResponseEntity.ok(update);
  }

  //채널 삭제
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  //특정 사용자가 볼 수 있는 모든 채널 목록을 조회
  @GetMapping("/{userId}/list")
  public ResponseEntity<List<ChannelResponseDTO>> getUserAllChannels(@PathVariable UUID userId) {
    List<ChannelResponseDTO> userChannelList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(userChannelList);
  }


}

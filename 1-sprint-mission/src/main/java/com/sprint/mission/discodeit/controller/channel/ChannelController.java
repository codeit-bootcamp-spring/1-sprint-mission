package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateDTO;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Channel Controller", description = "채널 관련 API 엔드포인트 관리")
public class ChannelController {

  private final ChannelService channelService;

  public ChannelController(ChannelService channelService) {
    this.channelService = channelService;
  }

  @Operation(summary = "공개 채널 등록", description = " 새로운 공개 채널 등록 및 채널 정보 반환")
  @PostMapping("/public")
  public ResponseEntity<ChannelResponseDTO> createChannel(
      @RequestBody @Valid PublicChannelCreateDTO createDTO) {
    ChannelResponseDTO publicChannel = channelService.createPublicChannel(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
  }

  @Operation(summary = "비공개 채널 등록", description = " 새로운 비공개 채널 등록 및 채널 정보 반환")
  @PostMapping("/private")
  public ResponseEntity<ChannelResponseDTO> createPrivateChannel(
      @RequestBody PrivateChannelCreateDTO createDTO) {
    ChannelResponseDTO privateChannel = channelService.createPrivateChannel(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
  }

  @Operation(summary = "공개 채널 정보 수정", description = "공개 채널 정보 수정 및 수정된 공개 채널 정보 반환")
  @PatchMapping("/public")
  public ResponseEntity<ChannelResponseDTO> updatePublicChannel(
      @RequestBody @Valid ChannelUpdateDTO updateDTO) {
    ChannelResponseDTO update = channelService.update(updateDTO);
    return ResponseEntity.ok(update);
  }

  @Operation(summary = "채널 삭제", description = "채널 삭제")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  //특정 사용자가 볼 수 있는 모든 채널 목록을 조회
  @Operation(summary = "특정 사용자가 볼 수 있는 채널 조회", description = "유저 아이디를 통해 특정 사용자가 볼 수 있는 모든 채널 목록을 조회")
  @GetMapping("/{userId}/list")
  public ResponseEntity<List<ChannelResponseDTO>> getUserAllChannels(@PathVariable UUID userId) {
    List<ChannelResponseDTO> userChannelList = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(userChannelList);
  }


}

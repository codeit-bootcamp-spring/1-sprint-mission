package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("public")
  public ApiResponse<UUID> createPublic(@RequestBody ChannelCreatePublicDTO request) {
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.CREATED.value())
        .message("public 채널 생성 완료")
        .data(channelService.create(request))
        .build();
  }

  @PostMapping("private")
  public ApiResponse<UUID> createPrivate(@RequestBody ChannelCreatePrivateDTO request) {
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.CREATED.value())
        .message("private 채널 생성 완료")
        .data(channelService.create(request))
        .build();
  }

  @PutMapping("{channelId}")
  public ApiResponse update(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateDTO request) {
    channelService.update(channelId, request);
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.OK.value())
        .message("채널 수정 완료")
        .build();
  }

  @DeleteMapping("{channelId}")
  public ApiResponse<UUID> delete(@PathVariable UUID channelId) {
    return ApiResponse.<UUID>builder()
        .code(HttpStatus.OK.value())
        .message("채널 삭제 완료")
        .data(channelService.delete(channelId))
        .build();
  }

  @GetMapping
  public ApiResponse<List<ChannelFindDTO>> findAllByUserId(@RequestParam("userId") UUID userId) {
    return ApiResponse.<List<ChannelFindDTO>>builder()
        .code(HttpStatus.OK.value())
        .message("사용자가 볼 수 있는 채널 목록")
        .data(channelService.findAllByUserId(userId))
        .build();
  }
}

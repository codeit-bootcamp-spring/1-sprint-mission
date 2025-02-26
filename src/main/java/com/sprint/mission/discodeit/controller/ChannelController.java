package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResponseDTO;
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
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/channels/pulblic")
    public ResponseDTO<UUID> createPublicChannel(@RequestBody ChannelCreatePublicDTO request){
        return ResponseDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("public 채널 생성 완료")
                .data(channelService.create(request))
                .build();
    }

    @PostMapping("/channels/private")
    public ResponseDTO<UUID> createPrivateChannel(@RequestBody ChannelCreatePrivateDTO request){
        return ResponseDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("private 채널 생성 완료")
                .data(channelService.create(request))
                .build();
    }

    @PutMapping("channels/{channelId}")
    public ResponseDTO updateChannel(@PathVariable UUID channelId, @RequestBody ChannelUpdateDTO request){
        channelService.update(channelId, request);
        return ResponseDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("채널 수정 완료")
                .build();
    }

    @DeleteMapping("channels/{channelId}")
    public ResponseDTO<UUID> deleteChannel(@PathVariable UUID channelId){
        return ResponseDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("채널 삭제 완료")
                .data(channelService.delete(channelId))
                .build();
    }

    @GetMapping("channels/{userId}")
    public ResponseDTO<List<ChannelFindDTO>> getUserChannels(@PathVariable UUID userId){
        return ResponseDTO.<List<ChannelFindDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("사용자가 볼 수 있는 채널 목록")
                .data(channelService.findAllByUserId(userId))
                .build();
    }
}

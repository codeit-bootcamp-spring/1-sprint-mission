package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelControllerDocs {
    private final ChannelService channelService;

    @PostMapping("/public")
    public CustomApiResponse<ChannelResponseDto> createPublicChannel(@RequestBody PublicChannelRequestDto channelReqDto) {
        return CustomApiResponse.created(channelService.createPublicChannel(channelReqDto));
    }

    @PostMapping("/private")
    public CustomApiResponse<ChannelResponseDto> createPrivateChannel(@RequestBody PrivateChannelRequestDto channelReqDto) {
        return CustomApiResponse.created(channelService.createPrivateChannel(channelReqDto));
    }

    @PutMapping("/{channelId}")
    public CustomApiResponse<ChannelResponseDto> updatePublicChannel(
            @PathVariable UUID channelId, @RequestBody ChannelUpdateDto updateDto) {
        return CustomApiResponse.ok(channelService.update(channelId, updateDto));
    }

    @DeleteMapping("/{channelId}")
    public CustomApiResponse<ChannelResponseDto> deleteChannel(@PathVariable UUID channelId) {
        return CustomApiResponse.ok(channelService.delete(channelId));
    }

    @GetMapping
    public CustomApiResponse<List<ChannelResponseDto>> getChannels(@RequestParam UUID userId) {
        return CustomApiResponse.ok(channelService.findAllByUserId(userId));
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.CommonDTO;
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
    public CustomApiResponse<CommonDTO.idResponse> createPublicChannel(@RequestBody ChannelDTO.request channelReqDto) {
        return CustomApiResponse.created(channelService.createPublicChannel(channelReqDto));
    }

    @PostMapping("/private")
    public CustomApiResponse<CommonDTO.idResponse> createPrivateChannel(@RequestBody ChannelDTO.request channelReqDto) {
        return CustomApiResponse.created(channelService.createPrivateChannel(channelReqDto));
    }

    @PutMapping("/{channelId}")
    public CustomApiResponse<CommonDTO.idResponse> updatePublicChannel(@PathVariable Long channelId, @RequestBody ChannelDTO.update updateDto) {
        return CustomApiResponse.ok(channelService.update(updateDto));
    }

    @DeleteMapping("/{channelId}")
    public CustomApiResponse<CommonDTO.idResponse> deleteChannel(@PathVariable Long channelId) {
        return CustomApiResponse.ok(channelService.delete(channelId));
    }

    @GetMapping
    public CustomApiResponse<List<ChannelDTO.response>> getChannels(@RequestParam UUID userId) {
        return CustomApiResponse.ok(channelService.findAllByUserId(userId));
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ApiResponse<ChannelDTO.idResponse> createPublicChannel(@RequestBody ChannelDTO.request channelReqDto) {
        return ApiResponse.created(channelService.createPublicChannel(channelReqDto));
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ApiResponse<ChannelDTO.idResponse> createPrivateChannel(@RequestBody ChannelDTO.request channelReqDto) {
        return ApiResponse.created(channelService.createPrivateChannel(channelReqDto));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResponse<ChannelDTO.idResponse> updatePublicChannel(@PathVariable Long id, @RequestBody ChannelDTO.update updateDto) {
        return ApiResponse.ok(channelService.update(updateDto));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiResponse<ChannelDTO.idResponse> deleteChannel(@PathVariable Long id) {
        return ApiResponse.ok(channelService.delete(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse<List<ChannelDTO.response>> getChannels(@RequestParam UUID userId) {
        return ApiResponse.ok(channelService.findAllByUserId(userId));
    }
}

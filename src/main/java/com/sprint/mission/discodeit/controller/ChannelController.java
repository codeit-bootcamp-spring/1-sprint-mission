package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.channelService.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelService.ChannelDTO;
import com.sprint.mission.discodeit.dto.channelService.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channelService.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.basic.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor

public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public Channel createPublicChannel(@RequestBody ChannelCreateRequest request) {
        return channelService.createPublicChannel(request);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public Channel createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        return channelService.createPrivateChannel(request);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ChannelDTO updateChannel(@RequestBody ChannelUpdateRequest request) {
        return channelService.update(request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteChannel(@PathVariable UUID id) {
        channelService.delete(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ChannelDTO> getUserChannels(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }



}

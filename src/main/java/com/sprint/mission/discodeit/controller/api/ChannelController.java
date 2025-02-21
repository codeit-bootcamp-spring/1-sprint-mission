package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/public")
    public ChannelResponse createPublicChannel(@RequestBody ChannelRequest.CreatePublic publicChannelRequest) {
        return channelService.createPublicChannel(publicChannelRequest);
    }

    @PostMapping("/private")
    public ChannelResponse createPrivateChannel(@RequestBody ChannelRequest.CreatePrivate privateChannelRequest) {
        return channelService.createPrivateChannel(privateChannelRequest);
    }

    @PutMapping("/public/{channelId}")
    public ChannelResponse updatePublicChannel(
            @PathVariable UUID channelId,
            @RequestBody ChannelRequest.Update publicChannelRequest
    ) {
        return channelService.update(channelId, publicChannelRequest);
    }

    @DeleteMapping("/{channelId}")
    public String deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteById(channelId);
        return "delete ok";
    }

    @GetMapping
    public List<ChannelResponse> getChannelListByUser(@RequestParam("userId") UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}

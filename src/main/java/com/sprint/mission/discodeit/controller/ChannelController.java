package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    public ChannelResponse createChannel(@RequestBody CreateChannelRequest request) {
        return channelService.createChannel(request);
    }

    @PostMapping("/private")
    public ChannelResponse createPrivateChannel(@RequestBody CreatePrivateChannelRequest request) {
        return channelService.createPrivateChannel(request);
    }

    @GetMapping
    public List<ChannelResponse> getChannels() {
        return channelService.getChannels();
    }

    @GetMapping("/{id}")
    public Optional<ChannelResponse> getChannel(@PathVariable UUID id) {
        return channelService.getChannel(id);
    }

    @PatchMapping("/{id}")
    public Optional<ChannelResponse> updateChannel(@PathVariable UUID id, @RequestParam String channelName) {
        return channelService.updateChannel(id, channelName);
    }

    @DeleteMapping("/{id}")
    public void deleteChannel(@PathVariable UUID id) {
        channelService.deleteChannel(id);
    }
}

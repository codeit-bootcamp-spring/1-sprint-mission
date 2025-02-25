package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.dto.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.channel.CreateChannel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping( "/channels/public")
    public ResponseEntity<Channel> createPublicChannel(@RequestBody CreateChannel.PublicRequest request) {
        Channel publicChannel = channelService.createPublicChannel(request);
        return ResponseEntity.ok(publicChannel);
    }

    @PostMapping("/channels/private")
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody CreateChannel.PrivateRequest request) {
        Channel privateChannel = channelService.createPrivateChannel(request);
        return ResponseEntity.ok(privateChannel);
    }

    @GetMapping("/channels/public")
    public ResponseEntity<ChannelDTO.PublicChannelDTO> getPublicChannel(@RequestParam("id") UUID uuid) {
        ChannelDTO.PublicChannelDTO publicChannel = channelService.findPublicChannel(uuid);
        return ResponseEntity.ok(publicChannel);
    }

    @GetMapping("/channels/private")
    public ResponseEntity<ChannelDTO.PrivateChannelDTO> getPrivateChannel(@RequestParam("id") UUID uuid) {
        ChannelDTO.PrivateChannelDTO privateChannel = channelService.findPrivateChannel(uuid);
        return ResponseEntity.ok(privateChannel);
    }

    @GetMapping("/channels")
    public ResponseEntity<List<Channel>> getAllPrivateChannel(@RequestParam("id") UUID userId) {
        List<Channel> allPrivate = channelService.findAllPrivate(userId);
        return ResponseEntity.ok(allPrivate);
    }

    @GetMapping("/channels/all")
    public ResponseEntity<List<Channel>> getAllPrivateChannel() {
        List<Channel> allChannel = channelService.findAll();
        return ResponseEntity.ok(allChannel);
    }

    @DeleteMapping("/channels/private")
    public ResponseEntity<String> deletePrivateChannel(@RequestParam("id") UUID uuid) {
        Channel removeChannel = channelService.deletePrivate(uuid);
        return ResponseEntity.ok(
                "Remove Private Channel ID: " + removeChannel.getId() +
                        " delete complete!"
        );
    }

    @DeleteMapping("/channels/public")
    public ResponseEntity<String> deletePublicChannel(@RequestParam("id") UUID uuid) {
        Channel removeChannel = channelService.deletePublic(uuid);
        return ResponseEntity.ok(
                "Remove Public Channel ID: " + removeChannel.getId() +
                        " delete complete!"
        );
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDetailResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<Void> createChannel(@RequestBody PublicChannelRequest publicChannelRequest) {
        Channel publicChannel = channelService.createPublicChannel(publicChannelRequest);
        return ResponseEntity.created(URI.create("channels/" + publicChannel.getId())).build();
    }

    @PostMapping("/private")
    public ResponseEntity<Void> createPrivateChannel(@RequestBody PrivateChannelRequest privateChannelRequest) {
        Channel privateChannel = channelService.createPrivateChannel(privateChannelRequest.userId());
        return ResponseEntity.created(URI.create("channels/" + privateChannel.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<ChannelDetailResponse>> getChannels(@RequestParam UUID userId) {
        return ResponseEntity.ok(channelService.readAllByUserId(userId));
    }

    @PutMapping("/public/{id}")
    public ResponseEntity<Void> updatePublicChannel(@PathVariable UUID id, @RequestBody PublicChannelRequest publicChannelRequest) {
        channelService.updateChannel(id, publicChannelRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/users/{userId}")
    public ResponseEntity<Void> addUser(@PathVariable UUID id, @PathVariable UUID userId) {
        channelService.addUser(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, @PathVariable UUID userId) {
        channelService.deleteUser(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }
}

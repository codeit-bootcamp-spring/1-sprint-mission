package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<ChannelDTO> createChannel(@RequestBody ChannelCreateDTO channelCreateDTO) {
        ChannelDTO createdChannel = channelService.createChannel(channelCreateDTO);
        return ResponseEntity.created(URI.create("/api/channels/" + createdChannel.getId()))
                .body(createdChannel);
    }

    @GetMapping
    public ResponseEntity<List<ChannelDTO>> getAllChannels() {
        return ResponseEntity.ok(channelService.readAll());
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDTO> getChannelById(@PathVariable UUID channelId) {
        return channelService.read(channelId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{channelId}")
    public ResponseEntity<Void> update(@PathVariable UUID channelId, @RequestBody ChannelUpdateDTO channelUpdateDTO) {
        channelService.update(channelId, channelUpdateDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelDTO>> getUserChannels(@PathVariable UUID userId) {
        return ResponseEntity.ok(channelService.getChannelsForUser(userId));
    }
}

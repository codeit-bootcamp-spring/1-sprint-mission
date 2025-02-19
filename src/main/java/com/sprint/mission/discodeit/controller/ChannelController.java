package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    @GetMapping
    public ResponseEntity<List<Channel>> getAllChannels(){
        return ResponseEntity.ok(channelService.findAll());
    }

    @PostMapping("/public")
    public ResponseEntity<Channel> createPublicChannel(@RequestBody ChannelDTO createDto) {
        return ResponseEntity.ok(channelService.createChannelPublic(createDto));
    }
    @PostMapping("/private")
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody ChannelDTO createDTO) {
        return ResponseEntity.ok(channelService.createChannelPrivate(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Channel> updateChannel(@PathVariable UUID id, @RequestBody ChannelDTO dto) {
        return ResponseEntity.ok(channelService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

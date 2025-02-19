package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.service.Interface.ChannelService;
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

    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createChannel(@RequestBody PublicChannelCreateRequestDto request) {
        return ResponseEntity.ok(channelService.createPublicChannel(request));
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createChannel(@RequestBody PrivateChannelCreateRequestDto request) {
        return ResponseEntity.ok(channelService.createPrivateChannel(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateChannel(@PathVariable UUID id, @RequestBody ChannelUpdateRequestDto request) {
        channelService.updateChannel(id, request);
        return ResponseEntity.ok("Updated channel successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChannel(@PathVariable UUID id) {
        channelService.deleteChannel(id);
        return ResponseEntity.ok("Deleted channel successfully.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelDto>> getAllChannelsByUserId(@PathVariable UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }
    @GetMapping("/all")
    public ResponseEntity<List<ChannelDto>> getAllChannels() {
        List<ChannelDto> channels = channelService.getAllChannels();
        return ResponseEntity.ok(channels);
    }

}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/create/public")
    public ResponseEntity<Channel> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        Channel createdChannel = channelService.create(request);
        return ResponseEntity.ok(createdChannel);
    }

    @PostMapping("/create/private")
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel createdChannel = channelService.create(request);
        return ResponseEntity.ok(createdChannel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelDto> getChannel(@PathVariable UUID id) {
        return ResponseEntity.ok(channelService.find(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelDto>> getChannelsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Channel> updateChannel(
            @PathVariable UUID id,
            @RequestBody PublicChannelUpdateRequest request
    ) {
        Channel updatedChannel = channelService.update(id, request);
        return ResponseEntity.ok(updatedChannel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

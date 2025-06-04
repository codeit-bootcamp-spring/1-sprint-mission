package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.service.Interface.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @Override
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createChannel(
            @Valid @RequestBody PublicChannelCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channelService.createPublicChannel(request));
    }

    @Override
    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createChannel(
            @Valid @RequestBody PrivateChannelCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(channelService.createPrivateChannel(request));
    }

    @Override
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable UUID channelId,
            @Valid @RequestBody ChannelUpdateRequestDto request) {
        return ResponseEntity.ok(channelService.updateChannel(channelId, request));
    }

    @Override
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }
}

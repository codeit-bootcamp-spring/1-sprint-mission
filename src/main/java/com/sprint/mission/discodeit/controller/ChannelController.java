package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
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
@RequiredArgsConstructor
@RequestMapping("api/channels")
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @PostMapping(value = "/public")
    public ResponseEntity<ChannelDto> createPublicChannel(
        @RequestBody CreatePublicChannelRequest createPublicChannelRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(channelService.createPublicChannel(createPublicChannelRequest));
    }

    @PostMapping(value = "/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(
        @RequestBody CreatePrivateChannelRequest createPrivateChannelRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(channelService.createPrivateChannel(createPrivateChannelRequest));
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<ChannelDto> updateChannel(
        @PathVariable UUID id,
        @RequestBody UpdateChannelRequest updateChannelRequest) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(channelService.updateChannel(id, updateChannelRequest));
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.deleteChannel(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "")
    public ResponseEntity<List<ChannelDto>> findUserAccessibleChannels(
        @RequestParam("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(channelService.findAllChannelsByUserId(userId));
    }
}
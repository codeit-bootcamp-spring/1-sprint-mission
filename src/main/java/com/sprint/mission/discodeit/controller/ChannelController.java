package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/create/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelDTO> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.ok(new ChannelDTO(
                channel.getId(),
                channel.getAdmin().getId(),
                channel.getType(),
                channel.getChannelName(),
                channel.getMemberList(),
                channel.getCreatedAt()
        ));
    }

    @RequestMapping(value = "/create/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelDTO> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.ok(new ChannelDTO(
                channel.getId(),
                channel.getAdmin().getId(),
                channel.getType(),
                channel.getChannelName(),
                channel.getMemberList(),
                channel.getCreatedAt()
        ));
    }

    @RequestMapping(value = "/update/{channelId}/{adminId}", method = RequestMethod.PUT)
    public ResponseEntity<ChannelDTO> updateChannel(
            @PathVariable UUID channelId,
            @PathVariable UUID adminId,
            @RequestBody PublicChannelUpdateRequest request) {
        Channel channel = channelService.update(channelId, adminId, request);
        return ResponseEntity.ok(new ChannelDTO(
                channel.getId(),
                channel.getAdmin().getId(),
                channel.getType(),
                channel.getChannelName(),
                channel.getMemberList(),
                channel.getCreatedAt()
        ));
    }

    @RequestMapping(value = "/delete/{channelId}/{adminId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(
            @PathVariable UUID channelId,
            @PathVariable UUID adminId) {
        channelService.delete(channelId, adminId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDTO>> getUserChannels(@PathVariable UUID userId) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }
}

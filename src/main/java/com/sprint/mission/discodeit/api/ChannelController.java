package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<Channel> updatePublicChannel(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        Channel updatedChannel = channelService.update(channelId, request);
        return ResponseEntity.ok(updatedChannel);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> getChannelsForUser(@PathVariable UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }
}

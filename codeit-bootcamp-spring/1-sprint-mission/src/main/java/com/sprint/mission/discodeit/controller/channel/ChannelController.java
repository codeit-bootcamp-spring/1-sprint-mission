package com.sprint.mission.discodeit.controller.channel;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @RequestMapping(value = "/all/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> getAll(
            @PathVariable(name = "id") UUID userId
    ) {
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }

    @RequestMapping(name = "/public", method = RequestMethod.POST)
    public ResponseEntity<Void> createPublicChannel(
            @RequestBody PublicChannelCreateRequest publicChannelCreateRequest
    ) {
        channelService.create(publicChannelCreateRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @RequestMapping(name = "/private", method = RequestMethod.POST)
    public ResponseEntity<Void> createPrivateChannel(
            @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest
    ) {
        channelService.create(privateChannelCreateRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateChannel(
            @PathVariable(name = "id") UUID channelId,
            @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest
    ) {
        channelService.update(channelId, publicChannelUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(
            @PathVariable(name = "id") UUID channelId
    ) {
        channelService.delete(channelId);
        return ResponseEntity.ok().build();
    }
}

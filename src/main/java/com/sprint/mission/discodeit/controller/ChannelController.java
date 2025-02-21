package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(value = "/public",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        method = RequestMethod.POST)
    public ResponseEntity<Channel> registerPublicChannel(
        @RequestBody PublicChannelCreateRequest publicChannelCreateRequest
    ) {
        Channel createdChannel = channelService.create(publicChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @RequestMapping(value = "/private",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        method = RequestMethod.POST)
    public ResponseEntity<Channel> registerPrivateChannel(
        @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest
    ) {
        Channel createdChannel = channelService.create(privateChannelCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @RequestMapping(value = "/public/{channelId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        method = RequestMethod.PUT)
    public ResponseEntity<Channel> updatePublicChannel(
        @PathVariable UUID channelId,
        @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest
    ) {
        Channel updatedChannel = channelService.update(channelId, publicChannelUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findAllChannel(@PathVariable UUID userId) {
        List<ChannelDto> allChannel = channelService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(allChannel);
    }
}

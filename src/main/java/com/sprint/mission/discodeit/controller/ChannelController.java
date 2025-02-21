package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping( value = "/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelPublicResponse> createPublicChannel(@RequestBody ChannelPublicRequest channelPublicRequest){
        ChannelPublicResponse channelPublicResponse = channelService.createPublicChannel(channelPublicRequest);
        return ResponseEntity.ok(channelPublicResponse);
    }

    @RequestMapping( value = "/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelPrivateResponse> createPrivateChannel(@RequestBody ChannelPrivateRequest channelPrivateRequest){
        ChannelPrivateResponse channelPrivateResponse = channelService.createPrivateChannel(channelPrivateRequest);
        return ResponseEntity.ok(channelPrivateResponse);
    }

    @RequestMapping(value = "/public/{channelId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updatePublicChannel(@PathVariable UUID channelId, @RequestBody ChannelUpdateRequest channelUpdateRequest){
        channelService.updateChannel(channelId, channelUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{channelType}/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId){
        channelService.deleteChannelById(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}/channel-list", method = RequestMethod.GET)
    public ResponseEntity<Collection<ChannelFindAllResponse>> getChannelListByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(channelService.findAllByUserId(userId));
    }

}

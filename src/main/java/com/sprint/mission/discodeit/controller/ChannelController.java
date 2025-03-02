package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;


    @RequestMapping(value = "/createPublic", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponse> createPublicChannel(@RequestBody ChannelCreateRequest request) {
        ChannelResponse createdChannel = channelService.createPublicChannel(
                request.channelName(),
                request.description());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @RequestMapping(value = "/createWithType", method = RequestMethod.POST)
    public ResponseEntity<ChannelResponse> createChannelWithType(
            @RequestParam("type") ChannelType type,
            @RequestBody ChannelCreateRequest request) {
        ChannelResponse createdChannel = channelService.createChannel(
                request.channelName(),
                request.description(),
                type);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ChannelResponse> getChannelById(@RequestParam("channelid") UUID channelId) {
        ChannelResponse channel = channelService.getChannelById(channelId);
        if (channel == null) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다: " + channelId);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channel);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelResponse>> getAllChannels() {
        List<ChannelResponse> channels = channelService.getAllChannels();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channels);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<ChannelResponse> updateChannel(
            @RequestParam("channelid") UUID channelId,
            @RequestBody ChannelUpdateRequest request) {
        ChannelResponse updatedChannel = channelService.updateChannel(channelId, request);
        if (updatedChannel == null) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다: " + channelId);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedChannel);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChannel(@RequestParam("channelid") UUID channelId) {
        boolean deleted = channelService.deleteChannel(channelId);
        if (deleted) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } else {
            throw new NoSuchElementException("채널을 찾을 수 없습니다: " + channelId);
        }
    }
}
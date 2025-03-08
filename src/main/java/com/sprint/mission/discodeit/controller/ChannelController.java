package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/createPublic")
    public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.createChannel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channel);
    }
    
    @RequestMapping(value = "/createPrivate")
    public ResponseEntity<Channel> create(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.createChannel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channel);
    }
    @RequestMapping(value = "/update")
    public ResponseEntity<Channel> update(@RequestParam("channelId") UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        Channel channel = channelService.updateChannelField(channelId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channel);
    }

    @RequestMapping(value = "/delete")
    public ResponseEntity<Void> delete(@RequestParam("channelId") UUID channelId) {
        channelService.deleteChannelById(channelId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @RequestMapping(value = "/findAll")
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
        List<ChannelDto> channelDtoList = channelService.readAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channelDtoList);
    }

}

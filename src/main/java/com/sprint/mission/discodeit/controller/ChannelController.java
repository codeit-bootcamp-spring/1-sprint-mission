package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/channel")
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(path = "createPublic")
    public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest request) {
        Channel createdChannel = channelService.createPublicChannel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @RequestMapping(path = "createPrivate")
    public ResponseEntity<Channel> create(@ModelAttribute PrivateChannelCreateRequest request) {
        Channel createdChannel = channelService.createPrivateChannel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @RequestMapping(path = "update")
    public ResponseEntity<Void> update(@RequestParam("channelId") UUID channelId,
                                          @ModelAttribute ChannelUpdateRequest request) {
        channelService.update(channelId, request);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @RequestMapping(path = "delete")
    public ResponseEntity<Void> delete(@RequestParam("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @RequestMapping(path = "findAll")
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channels);
    }
}

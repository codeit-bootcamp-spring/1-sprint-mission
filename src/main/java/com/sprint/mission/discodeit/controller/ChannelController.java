package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdate;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(path = "createPublic")
    public ResponseEntity<Channel> create(@RequestBody PublicChannelCreate publicChannelCreate) {
        Channel createdChannel = channelService.createChannelPublic(publicChannelCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @RequestMapping(path = "createPrivate")
    public ResponseEntity<Channel> create(@RequestBody PrivateChannelCreate privateChannelCreate) {
        Channel createdChannel = channelService.createChannelPrivate(privateChannelCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdChannel);
    }

    @RequestMapping(path = "update")
    public ResponseEntity<Channel> update(@RequestParam("channelId") UUID channelId,
                                          @RequestBody PublicChannelUpdate publicChannelUpdate) {
        Channel udpatedChannel = channelService.update(channelId, publicChannelUpdate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(udpatedChannel);
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
        List<ChannelDto> channels = channelService.findAllByUserID(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channels);
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelAPI;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdate;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelAPI {
    private ChannelService channelService;

    @PostMapping("/public") //public channel 만들기
    public ResponseEntity<Channel> createPublicChannel(@RequestBody PublicChannelCreate publicChannelCreate) {
        Channel channel = channelService.createChannelPublic(publicChannelCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @PostMapping("/private") //private channel 만들기
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody PrivateChannelCreate privateChannelCreate) {
        Channel channel = channelService.createChannelPrivate(privateChannelCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }
    @GetMapping
    public ResponseEntity<List<ChannelDto>> getChannelsByUserId(@RequestParam("userId") UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserID(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
    @PatchMapping("/{channelId}")
    public ResponseEntity<Channel> updateChannel(@PathVariable("channelId") UUID channelId, @RequestBody PublicChannelUpdate publicChannelUpdate) {
        Channel updatedChannel = channelService.update(channelId, publicChannelUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
    }
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

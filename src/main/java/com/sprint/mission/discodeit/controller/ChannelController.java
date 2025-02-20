package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdate;
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
    private ChannelService channelService;

    @PostMapping("/public") //public channel 만들기
    public ResponseEntity<Channel> createPublicChannel(@RequestBody PublicChannelCreate publicChannelCreate) {
        Channel channel = channelService.createChannelPublic(publicChannelCreate);
        return ResponseEntity.ok(channel);
    }

    @PostMapping("/private") //private channel 만들기
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody PrivateChannelCreate privateChannelCreate) {
        Channel channel = channelService.createChannelPrivate(privateChannelCreate);
        return ResponseEntity.ok(channel);
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDto> getChannelById(@PathVariable UUID channelId) {
        ChannelDto channel = channelService.findById(channelId);
        return ResponseEntity.ok(channel);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelDto>> getChannelsByUserId(@PathVariable UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserID(userId);
        return ResponseEntity.ok(channels);
    }
    @PutMapping("/{channelId}")
    public ResponseEntity<Channel> updateChannel(@PathVariable UUID channelId, @RequestBody PublicChannelUpdate publicChannelUpdate) {
        Channel updatedChannel = channelService.update(channelId, publicChannelUpdate);
        return ResponseEntity.ok(updatedChannel);
    }
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

}

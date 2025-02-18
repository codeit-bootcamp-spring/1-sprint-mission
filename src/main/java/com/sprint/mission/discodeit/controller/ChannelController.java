package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/public/create")
    public ResponseEntity<ChannelDto> createPublicChannel(@RequestBody PublicChannelCreateRequest request){
        Channel channel = channelService.create(request);
        URI location = URI.create("/channels/public/" + channel.getId());
        return ResponseEntity.created(location).body(channelService.find(channel.getId()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/private/create")
    public ResponseEntity<ChannelDto> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        URI location = URI.create("/channels/private/" + channel.getId());
        return ResponseEntity.created(location).body(channelService.find(channel.getId()));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/public/update/{channelId}")
    public ResponseEntity<ChannelDto> updatePublicChannel(@PathVariable UUID channelId, PublicChannelUpdateRequest request){
        Channel channel = channelService.update(channelId, request);
        return ResponseEntity.ok(channelService.find(channel.getId()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/delete/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId){
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/all")
    public ResponseEntity<List<ChannelDto>> getAllChannelsByUser(@PathVariable UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels); // 사용자가 볼 수 있는 모든 채널 목록 반환
    }
}

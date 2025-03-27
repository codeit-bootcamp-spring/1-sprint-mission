package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.FindPrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.FindPublicChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.UpdatePublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<FindChannelResponseDto> createPublicChannel(@RequestBody CreatePublicChannelRequestDto createPublicChannelRequestDto) {

        UUID id = channelService.createPublic(createPublicChannelRequestDto);
        Channel channel = channelService.find(id);

        return ResponseEntity.created(URI.create("/api/channel/" + id)).body(FindPublicChannelResponseDto.fromEntity(channel));
    }

    @PostMapping("/private")
    public ResponseEntity<FindChannelResponseDto> createPrivateChannel(@RequestParam UUID ownerId) {

        CreatePrivateChannelRequestDto createPrivateChannelDto = new CreatePrivateChannelRequestDto(ownerId);
        UUID id = channelService.createPrivate(createPrivateChannelDto);
        Channel channel = channelService.find(id);

        return ResponseEntity.created(URI.create("/api/channel/" + id)).body(FindPrivateChannelResponseDto.fromEntity(channel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FindChannelResponseDto> updatePublicChannel(@RequestBody UpdatePublicChannelRequestDto updatePublicChannelRequestDto) {
        FindChannelResponseDto findChannelResponseDto = channelService.updateChannel(updatePublicChannelRequestDto);

        return ResponseEntity.ok(findChannelResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FindChannelResponseDto>> findChannel(@PathVariable UUID userId) {
        List<FindChannelResponseDto> channels = channelService.findAllByUserId(userId);

        return ResponseEntity.ok(channels);
    }
}

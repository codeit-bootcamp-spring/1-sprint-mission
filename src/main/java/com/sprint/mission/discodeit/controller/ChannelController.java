package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channelDto.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channelDto.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channelDto.FindChannelResponseDto;
import com.sprint.mission.discodeit.dto.channelDto.UpdatePublicChannelRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST, value = "/public")
    public ResponseEntity<FindChannelResponseDto> createPublicChannel(@RequestParam UUID ownerId,
                                                                      @RequestParam String name,
                                                                      @RequestParam String explanation) {

        CreatePublicChannelRequestDto createPublicChannelDto = new CreatePublicChannelRequestDto(ownerId, name, explanation);
        UUID id = channelService.createPublic(createPublicChannelDto);

        return ResponseEntity.created(URI.create("/api/channel/" + id)).body(channelService.find(id));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/private")
    public ResponseEntity<FindChannelResponseDto> createPrivateChannel(@RequestParam UUID ownerId) {

        CreatePrivateChannelRequestDto createPrivateChannelDto = new CreatePrivateChannelRequestDto(ownerId);
        UUID id = channelService.createPrivate(createPrivateChannelDto);

        return ResponseEntity.created(URI.create("/api/channel/" + id)).body(channelService.find(id));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<FindChannelResponseDto> updatePublicChannel(@PathVariable UUID id,
                                                                      @RequestParam String category,
                                                                      @RequestParam String name,
                                                                      @RequestParam String explanation) {
        UpdatePublicChannelRequestDto updatePublicChannelDto = new UpdatePublicChannelRequestDto(id, category, name, explanation);
        FindChannelResponseDto findChannelResponseDto = channelService.updateChannel(updatePublicChannelDto);

        return ResponseEntity.ok(findChannelResponseDto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}")
    public ResponseEntity<List<FindChannelResponseDto>> findChannel(@PathVariable UUID userId) {
        List<FindChannelResponseDto> channels = channelService.findAllByUserId(userId);

        return ResponseEntity.ok(channels);
    }
}

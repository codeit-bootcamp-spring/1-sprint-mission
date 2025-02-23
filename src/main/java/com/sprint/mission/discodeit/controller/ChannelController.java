package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PrivateChannelCreateResponse;
import com.sprint.mission.discodeit.dto.response.PublicChannelCreateResponse;
import com.sprint.mission.discodeit.dto.response.PublicChannelUpdateResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final UserService userService;

    @RequestMapping(value = "/public", method = RequestMethod.POST) //String name,String description
    public ResponseEntity<PublicChannelCreateResponse> createPublic(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        PublicChannelCreateResponse createResponse = PublicChannelCreateResponse.from(channel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @RequestMapping(value = "/private", method = RequestMethod.POST)//List<UUID> participantIds
    public ResponseEntity<PrivateChannelCreateResponse> createPrivate(@RequestBody PrivateChannelCreateRequest request) {
        Channel channel = channelService.create(request);
        PrivateChannelCreateResponse createResponse = PrivateChannelCreateResponse.from(channel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @RequestMapping(value = "/public/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<PublicChannelUpdateResponse> update(
            @PathVariable UUID channelId,
            @RequestBody PublicChannelUpdateRequest request
    ) {
//        try {
        Channel channel = channelService.update(channelId, request);
//        }catch (NoSuchElementException e){//TODO: 전역 예외 처리
//        }catch (IllegalArgumentException e){
//        }
        PublicChannelUpdateResponse createResponse = PublicChannelUpdateResponse.from(channel);
        return ResponseEntity.status(HttpStatus.OK).body(createResponse);
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> findChannelsByUser(@PathVariable UUID userId){
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }
}
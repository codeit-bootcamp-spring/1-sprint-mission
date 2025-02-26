package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.ErrorResponse;
import com.spirnt.mission.discodeit.dto.channel.*;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.service.ChannelService;
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

    // 공개 채널 생성
    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ResponseEntity<?> creatChannelPublic(@RequestBody PublicChannelCreateRequest request) {
        Channel channel = channelService.createChannelPublic(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ChannelCreateResponse(channel.getId()));
    }

    // 비공개 채널 생성
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ResponseEntity<?> createChannelPrivate(@RequestBody PrivateChannelRequest request) {
        if (request.getUsersId().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("There are no participants."));
        }
        Channel channel = channelService.createChannelPrivate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ChannelCreateResponse(channel.getId()));
    }

    // 공개 채널 정보 수정
    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePublicChannel(@PathVariable UUID channelId,
                                                 @RequestBody ChannelUpdateRequest request) {
        if (request.getName() == null && request.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("You should enter at least one element to update."));
        }
        channelService.update(channelId, request);
        return ResponseEntity.ok().build();
    }

    // 채널 삭제
    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok().build();
    }


    // 특정 사용자가 볼 수 있는 채널 목록 조회
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllChannelsByUserId(@PathVariable UUID userId) {
        List<ChannelResponse> channelResponses = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channelResponses);
    }

}

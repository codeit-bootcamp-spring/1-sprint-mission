package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    //  공개 채널 생성
    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public ChannelDto createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        return channelService.create(request);
    }

    //  비공개 채널 생성
    @RequestMapping(value = "/private", method = RequestMethod.POST)
    public ChannelDto createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        return channelService.create(request);
    }

    //  공개 채널 정보 수정
    @RequestMapping(value = "/{channelId}", method = RequestMethod.PUT)
    public ChannelDto updatePublicChannel(@PathVariable UUID channelId, @RequestBody PublicChannelUpdateRequest request) {
        return channelService.update(channelId, request);
    }

    //  채널 삭제
    @RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
    public void deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
    }

    //  특정 사용자가 볼 수 있는 모든 채널 목록 조회
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<ChannelDto> getAllChannelsForUser(@PathVariable UUID userId) {
        return channelService.findAllByUserId(userId);
    }

}

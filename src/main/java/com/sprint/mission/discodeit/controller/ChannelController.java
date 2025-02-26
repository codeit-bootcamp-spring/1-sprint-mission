package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    // 채널 생성
    @RequestMapping(method = RequestMethod.POST)
    public ChannelDto createChannel(@RequestBody ChannelDto channelDto) {
        return channelService.createChannel(channelDto);
    }

    // 단일 채널 조회
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ChannelDto getChannel(@PathVariable("id") UUID id) {
        return channelService.findChannelById(id);
    }

    // 전체 채널 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<ChannelDto> getAllChannels() {
        return channelService.findAllChannels();
    }

    // 채널 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ChannelDto updateChannel(@PathVariable("id") UUID id, @RequestBody ChannelDto channelDto) {
        return channelService.updateChannel(id, channelDto);
    }

    // 채널 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteChannel(@PathVariable("id") UUID id) {
        channelService.deleteChannel(id);
    }
}

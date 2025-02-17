package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    // todo - 공개 채널 생성
    @PostMapping
    public ChannelResponseDto creatPublicChannel(@RequestBody CreateChannelDto createChannelDto) {
        return channelService.create(createChannelDto);
    }

    // todo - 비공개 채널 생성 (수정 필요 - public이랑 겹침)
    @PostMapping
    public ChannelResponseDto creatPrivateChannel(@RequestBody CreateChannelDto createChannelDto, List<String> userIds){
        return channelService.create(createChannelDto, userIds);
    }

    // todo - 공개 채널 정보 수정
    @PutMapping("/{channelId}")
    public ChannelResponseDto updatePublicChannel(@PathVariable String channelId, @RequestBody UpdateChannelDto updateChannelDto) {
        return channelService.updateChannel(channelId, updateChannelDto);
    }

    // todo -  채널 삭제
    @DeleteMapping("/{channelId}")
    public String deletePublicChannel(@PathVariable String channelId) {
        if(channelService.delete(channelId)){
            return "Channel deleted successfully";
        }
        return "Channel deletion failed";
    }

    // todo - 특정 사용자의 모든 채널 목록 조회
    @GetMapping("/users/{userId}")
    public List<ChannelResponseDto> getPublicChannel(@PathVariable String userId) {
        return channelService.findAllByUserId(userId);
    }
}

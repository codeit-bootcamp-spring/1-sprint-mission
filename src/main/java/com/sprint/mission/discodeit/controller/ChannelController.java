package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }
    
    //공개 채널 생성
    @PostMapping
    public String createPublicChannel(@RequestBody ChannelCreateDTO channelCreateDTO) {
        channelService.createPublicChannel(channelCreateDTO);
        return "Public channel created";
    }
    
    //비공개 채널 생성
    @PostMapping("/private")
    public String createPrivateChannel(@RequestBody PrivateChannelCreateDTO channelCreateDTO) {
        channelService.createPrivateChannel(channelCreateDTO);
        return "Private channel created";
    }

    //공개 채널 정보 수정
    @PatchMapping("/{id}")
    public String updateChannel(@PathVariable("id") UUID id, @RequestBody ChannelUpdateDTO channelUpdateDTO) {
        channelService.update(channelUpdateDTO);
        return "Channel updated";
    }
    
    //채널 삭제
    @DeleteMapping("/{id}")
    public String deleteChannel(@PathVariable("id") UUID id) {
        channelService.deleteChannel(id);
        return "Channel deleted";
    }


    //특정 사용자가 볼 수 있는 모든 채널 목록 조회
    @GetMapping("/{userId}")
    public List<ChannelFindDTO> findAllByUserId(@PathVariable("userId") UUID id) {
        return channelService.findAllByUserId(id);
    }

    @GetMapping
    public List<ChannelFindDTO> findAll(){
        return channelService.findAllDTO();
    }

}

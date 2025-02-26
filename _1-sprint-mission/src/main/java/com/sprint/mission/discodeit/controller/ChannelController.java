package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    //공개 채널 생성
    @RequestMapping(path = "createPublic")
    public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateDto publicChannelDto){
        Channel publicChannel = channelService.create(publicChannelDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
    }
    //비공개 채널 생성
    @RequestMapping(path="createPrivate")
    public ResponseEntity<Channel> createPrivate(@RequestBody PrivateChannelCreateDto privateChannelDto){
        Channel privateChannel = channelService.create(privateChannelDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
    }
    //공개 채널 정보 수정
    @RequestMapping(path = "update")
    public ResponseEntity<Channel> update(@RequestParam("channelId") UUID channelId, @RequestBody PublicChannelUpdateDto publicChannelUpdateDto){
        Channel updatedChannel = channelService.update(channelId,publicChannelUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
    }
    //채널 삭제
    @RequestMapping(path = "delete")
    public ResponseEntity<Void> delete(@RequestParam("channelId") UUID channelId){
        channelService.deleteById(channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //채널 목록 조회
    @RequestMapping(path = "findAll")
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId){
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}

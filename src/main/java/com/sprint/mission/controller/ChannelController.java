package com.sprint.mission.controller;


//import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.FindChannelAllDto;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.service.jcf.main.JCFChannelService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/channels")
public class ChannelController {

    private final JCFChannelService channelService;
    private final JCFUserService userService;

    @RequestMapping(path = "createPublic")
    public ResponseEntity<FindChannelDto> create(@RequestBody PublicChannelCreateDTO request) {
        Channel createdChannel = channelService.createPublicChannel(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(getFindChannelDto(createdChannel));
        //dto?
    }

    @RequestMapping(path = "createPrivate")
    public ResponseEntity<FindChannelDto> create(@RequestBody PrivateChannelCreateDTO request) {
        Channel createdChannel = channelService.createPrivateChannel(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(getFindChannelDto(createdChannel));
        //dto?
    }

    @RequestMapping(path = "update")
    public ResponseEntity<String> update(@RequestParam("channelId") UUID channelId,
        @RequestBody ChannelDtoForRequest requestDTO) {
        channelService.update(channelId, requestDTO);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("수정 완료");
    }

    @RequestMapping(path = "delete")
    public ResponseEntity<Void> delete(@RequestParam("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }


    //[ ] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
    @RequestMapping(path = "findAllByUserId")
    public ResponseEntity<List<FindChannelAllDto>> findAllByUserId(
        @RequestParam("userId") UUID userId) {

        List<FindChannelAllDto> channelDtoList = channelService.findAllByUserId(userId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelDtoList);
    }

    /**
     * 응답 DTO (타입별)
     */
    private FindChannelDto getFindChannelDto(Channel findedChannel) {
        return (findedChannel.getChannelType().equals(ChannelType.PRIVATE)
                ? new FindPrivateChannelDto(findedChannel)
                : new FindPublicChannelDto(findedChannel));
    }
}

package com.sprint.mission.controller;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
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

    @PostMapping
    public ResponseEntity<FindChannelDto> create(@RequestBody ChannelDtoForRequest requestDTO) {
        Channel createdChannel = channelService.create(requestDTO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(getFindChannelDto(createdChannel));
        //dto?
    }


    @PatchMapping("{id}")
    public ResponseEntity<String> update(@PathVariable UUID channelId
        , @RequestBody ChannelDtoForRequest requestDTO) {

        if (requestDTO.getChannelType().equals(ChannelType.PRIVATE)){
            throw new CustomException(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL);
        }

        channelService.update(channelId, requestDTO);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("수정 완료");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }


    @GetMapping
    public ResponseEntity<List<FindChannelDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
        List<Channel> channelList = channelService.findAllByUserId(userId);
        List<FindChannelDto> channelDtoList = channelList.stream()
            .map(this::getFindChannelDto).toList();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelDtoList);
    }

    @GetMapping("{id}")
    public ResponseEntity<FindChannelDto> findById(@PathVariable("id") UUID channelId) {
        Channel findChannel = channelService.findById(channelId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(getFindChannelDto(findChannel));
    }

//    @GetMapping(value = "/{usersId}/channels")
//    public ResponseEntity<List<FindChannelDto>> findAllByUserId(@PathVariable UUID userId) {
//        List<Channel> findAllChannel = channelService.findAllByUserId(userId);
//        List<FindChannelDto> findChannelDtoList = findAllChannel.stream()
//                .map(this::getFindChannelDto).collect(Collectors.toCollection(ArrayList::new));
//
//        return ResponseEntity.status(HttpStatus.OK).body(findChannelDtoList);
//    }


    /**
     * 응답 DTO (타입별)
     */
    private FindChannelDto getFindChannelDto(Channel findedChannel) {
        return (findedChannel.getChannelType().equals(ChannelType.PRIVATE)
                ? new FindPrivateChannelDto(findedChannel)
                : new FindPublicChannelDto(findedChannel));
    }

}

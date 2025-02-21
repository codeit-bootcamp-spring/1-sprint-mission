package com.sprint.mission.controller;


import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.service.jcf.main.JCFChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/channels")
public class ChannelController {

    private final JCFChannelService channelService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody ChannelDtoForRequest requestDTO) {
        channelService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Channel created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindChannelDto> findById(@PathVariable("id") UUID channelId) {
        Channel findChannel = channelService.findById(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(getFindChannelDto(findChannel));
    }

//    @GetMapping(value = "/{usersId}/channels")
//    public ResponseEntity<List<FindChannelDto>> findAllByUserId(@PathVariable UUID userId) {
//        List<Channel> findAllChannel = channelService.findAllByUserId(userId);
//        List<FindChannelDto> findChannelDtoList = findAllChannel.stream()
//                .map(this::getFindChannelDto).collect(Collectors.toCollection(ArrayList::new));
//
//        return ResponseEntity.status(HttpStatus.OK).body(findChannelDtoList);
//    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<String> update(@PathVariable UUID channelId, @RequestBody ChannelDtoForRequest requestDTO) {
        if (requestDTO.getChannelType().equals(ChannelType.PRIVATE)){
            return ResponseEntity.badRequest().body("PRIVATE 채널은 수정 불가능입니다.");
        }
        channelService.update(channelId, requestDTO);
        return ResponseEntity.ok("Successfully updated");
    }


    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok("Successfully deleted");
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

package com.sprint.mission.controller;


import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.request.UserDtoForRequest;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindPrivateChannelDto;
import com.sprint.mission.dto.response.FindPublicChannelDto;
import com.sprint.mission.dto.response.FindUserDto;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.jcf.main.JCFChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChannelController {

    private final JCFChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<String> save(@RequestBody ChannelDtoForRequest requestDTO) {
        channelService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Channel created successfully");
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<FindChannelDto> findById(@PathVariable UUID channelId) {
        Channel findChannel = channelService.findById(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(getFindChannelDto(findChannel));
    }

    @GetMapping("/channels/{userId}")
    public ResponseEntity<List<FindUserDto>> findAllByUserId(@PathVariable UUID userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<String> update(@PathVariable UUID userId, @RequestBody UserDtoForRequest requestDTO) {
        userService.update(userId, requestDTO);
        return ResponseEntity.ok("Successfully updated");
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable UUID userId) {
        userService.delete(userId);
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

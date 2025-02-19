package com.sprint.mission.controller;

import com.sprint.mission.dto.request.BinaryMessageContentDto;
import com.sprint.mission.dto.request.ChannelDtoForRequest;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.dto.response.FindChannelDto;
import com.sprint.mission.dto.response.FindMessageDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.service.jcf.addOn.BinaryMessageService;
import com.sprint.mission.service.jcf.main.JCFChannelService;
import com.sprint.mission.service.jcf.main.JCFMessageService;
import com.sprint.mission.service.jcf.main.JCFUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {


    private final JCFChannelService channelService;
    private final JCFMessageService messageService;
    private final JCFUserService userService;
    private final BinaryMessageService binaryMessageService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody MessageDtoForCreate requestDTO) {
        messageService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("M created successfully");
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<List<FindMessageDto>> findByChannelId(@PathVariable UUID channelId) {
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        List<FindMessageDto> messageDtoList = messageList.stream()
                .map((FindMessageDto::new))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(messageDtoList);
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<String> update(@PathVariable UUID messageId,
                                         @RequestBody MessageDtoForUpdate requestDTO) {
        requestDTO.setMessageId(messageId);
        messageService.update(requestDTO);
        return ResponseEntity.ok("Successfully updated");
    }


    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok("Successfully deleted");
    }
}

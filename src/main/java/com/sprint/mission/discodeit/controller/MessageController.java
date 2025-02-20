package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    //todo - 특정 채널 메세지 생성
    @PostMapping
    public MessageResponseDto getMessages(@RequestBody CreateMessageDto createMessageDto) {
        return messageService.create(createMessageDto);
    }

    //todo - 특정 채널 메세지 수정
    @PutMapping("/{messageId}")
    public MessageResponseDto updateMessage(@PathVariable String messageId, @RequestBody UpdateMessageDto updateMessageDto) {
        return messageService.updateMessage(messageId, updateMessageDto);
    }

    //todo - 특정 사용자의 모든 메세지 목록 조회
    @GetMapping("/users/{userId}")
    public List<MessageResponseDto> getMessages(@PathVariable String userId) {
        return messageService.findAllBySenderId(userId);
    }
}

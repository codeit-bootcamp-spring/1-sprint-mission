package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 메시지 생성
    @RequestMapping(method = RequestMethod.POST)
    public MessageDto createMessage(@RequestBody MessageDto messageDto) {
        return messageService.createMessage(messageDto);
    }

    // 단일 메시지 조회
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MessageDto getMessage(@PathVariable("id") UUID id) {
        return messageService.findMessageById(id);
    }

    // 특정 채널의 메시지 목록 조회
    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public List<MessageDto> getMessagesByChannel(@PathVariable("channelId") UUID channelId) {
        return messageService.findMessagesByChannelId(channelId);
    }

    // 메시지 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MessageDto updateMessage(@PathVariable("id") UUID id, @RequestBody MessageDto messageDto) {
        return messageService.updateMessage(id, messageDto);
    }

    // 메시지 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable("id") UUID id) {
        messageService.deleteMessage(id);
    }
}

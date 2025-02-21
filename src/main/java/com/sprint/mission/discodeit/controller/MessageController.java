package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //  메시지 생성
    @RequestMapping(method = RequestMethod.POST)
    public Message createMessage(@RequestBody MessageCreateRequest messageCreateRequest, @RequestBody(required = false)List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        return messageService.create(messageCreateRequest, binaryContentCreateRequests);
    }

    //  특정 메시지 조회
    @RequestMapping(value = "/{messageId}",method = RequestMethod.GET)
    public Message getMessage(@PathVariable UUID messageId) {
        return messageService.find(messageId);
    }

    //  특정 채널의 메시지 목록 조회
    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public List<Message> getMessagesByChannel(@PathVariable UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }

    //  메시지 수정
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public Message updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequest messageUpdateRequest) {
        return messageService.update(messageId, messageUpdateRequest);
    }

    //  메시지 삭제
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
    }
}

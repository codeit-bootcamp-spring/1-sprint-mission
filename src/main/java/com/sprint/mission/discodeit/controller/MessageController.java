package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageService.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageResponseDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    //  메시지 생성 API
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Message createMessage(@RequestBody MessageCreateRequestDTO request) {
        return messageService.create(request, List.of()); // 현재는 첨부파일 없이 메시지만 생성
    }

    //  특정 메시지 조회 API
    @RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
    public Message getMessage(@PathVariable UUID messageId) {
        return messageService.find(messageId);
    }

    //  특정 채널의 모든 메시지 조회 API
    @RequestMapping(method = RequestMethod.GET)
    public List<Message> getMessagesByChannel(@RequestParam UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }

    //  메시지 수정 API
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public Message updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequestDTO request) {
        return messageService.update(messageId, request);
    }

    //  메시지 삭제
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
    }


}

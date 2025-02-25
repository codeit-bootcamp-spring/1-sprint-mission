package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.create.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.message.create.MessageCreateRequestWithFiles;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")  // 기본 URL 경로
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST,value = "/create")  // /messages
    public ResponseEntity<Message> createMessage(@RequestBody MessageCreateRequestWithFiles messageWithAttachmentsRequest) {
        MessageCreateRequest messageCreateRequest = messageWithAttachmentsRequest.messageCreateRequest();
        List<BinaryContentCreateRequest> binaryContentCreateRequests = messageWithAttachmentsRequest.attachmentRequests();

        Message message = messageService.create(messageCreateRequest, binaryContentCreateRequests);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{messageId}/update")  // /messages/{messageId}
    public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequest request) {
        Message updatedMessage = messageService.update(messageId, request);
        return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{messageId}/delete")  // /messages/{messageId}
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/channel/{channelId}")  // /messages/channel/{channelId}
    public ResponseEntity<List<Message>> getMessagesByChannel(@PathVariable UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> createMessage(
            @RequestParam("senderid") UUID senderId,
            @RequestParam("channelid") UUID channelId,
            @RequestBody MessageCreateRequest request
    ) {
        Message createdMessage = messageService.createMessage(
                senderId,
                channelId,
                request.content()
        );
        if (createdMessage == null) {
            throw new NoSuchElementException("메시지를 생성할 수 없습니다");
        }
        MessageResponse response = MessageResponse.from(createdMessage);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse> getMessageById(@RequestParam("messageid") UUID messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            throw new NoSuchElementException("메시지를 찾을 수 없습니다: " + messageId);
        }
        MessageResponse response = MessageResponse.from(message);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @RequestMapping(value = "/listByChannel", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponse>> getMessagesByChannel(@RequestParam("channelid") UUID channelId) {
        List<Message> messages = messageService.getMessagesByChannel(channelId);
        List<MessageResponse> responses = new ArrayList<>();
        for (Message message : messages) {
            responses.add(MessageResponse.from(message));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    @RequestMapping(value = "/listBySender", method = RequestMethod.GET)
    public ResponseEntity<List<MessageResponse>> getMessagesBySender(@RequestParam("senderid") UUID senderId) {
        List<Message> messages = messageService.getMessagesBySender(senderId);
        List<MessageResponse> responses = new ArrayList<>();
        for (Message message : messages) {
            responses.add(MessageResponse.from(message));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> updateMessageContent(
            @RequestParam("messageid") UUID messageId,
            @RequestBody MessageUpdateRequest request
    ) {
        Message updatedMessage = messageService.updateMessageContent(messageId, request.updateContent());
        if (updatedMessage == null) {
            throw new NoSuchElementException("메시지를 찾을 수 없습니다: " + messageId);
        }
        MessageResponse response = MessageResponse.from(updatedMessage);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@RequestParam("messageid") UUID messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        if (deleted) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } else {
            throw new NoSuchElementException("메시지를 찾을 수 없습니다: " + messageId);
        }
    }
}
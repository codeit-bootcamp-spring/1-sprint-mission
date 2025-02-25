package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public MessageResponse createMessage(@RequestBody CreateMessageRequest request) {
        return messageService.createMessage(request);
    }

    @GetMapping
    public List<MessageResponse> getAllMessages() {
        return messageService.getMessages();
    }

    @GetMapping("/{id}")
    public Optional<MessageResponse> getMessageById(@PathVariable UUID id) {
        return messageService.getMessage(id);
    }

    @GetMapping("/channel/{channelId}")
    public List<MessageResponse> getMessagesByChannel(@PathVariable UUID channelId) {
        return messageService.getMessagesByChannel(channelId);
    }

    @PatchMapping
    public Optional<MessageResponse> updateMessage(@RequestBody UpdateMessageRequest request) {
        return messageService.updateMessage(request);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
    }
}

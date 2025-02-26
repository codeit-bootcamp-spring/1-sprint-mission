package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/create")
    public ResponseEntity<Message> createMessage(
            @RequestBody MessageCreateRequest messageCreateRequest,
            @RequestBody(required = false) List<BinaryContentCreateRequest> binaryContentCreateRequests
    ) {
        Message createdMessage = messageService.create(messageCreateRequest, binaryContentCreateRequests);
        return ResponseEntity.ok(createdMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.find(id));
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> getMessagesByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Message> updateMessage(
            @PathVariable UUID id,
            @RequestBody MessageUpdateRequest messageUpdateRequest
    ) {
        Message updatedMessage = messageService.update(id, messageUpdateRequest);
        return ResponseEntity.ok(updatedMessage);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

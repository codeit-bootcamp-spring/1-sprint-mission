package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.MessageRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDetailResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Void> createMessage(@RequestBody MessageRequest messageRequest) {
        Message message = messageService.createMessage(messageRequest);
        return ResponseEntity.created(URI.create("messages/" + message.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.readMessage(id));
    }

    @GetMapping
    public ResponseEntity<List<MessageDetailResponse>> getMessages(@RequestParam UUID channelId) {
        return ResponseEntity.ok(messageService.readAllByChannelId(channelId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMessage(@PathVariable UUID id, @RequestBody MessageUpdateRequest messageUpdateRequest) {
        messageService.updateMessage(id, messageUpdateRequest.content());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}

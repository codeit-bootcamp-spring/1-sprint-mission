package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/channel/{channelId}")
    public ResponseEntity<Message> sendMessage(@PathVariable UUID channelId , @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.createMessage(messageDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID id, @RequestBody MessageDTO dto) {
        return ResponseEntity.ok(messageService.update(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> getMessage(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findAllMessage());
    }
}

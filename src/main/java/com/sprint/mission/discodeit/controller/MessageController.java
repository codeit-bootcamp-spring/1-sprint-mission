package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody MessageCreateDto messageCreateDto, @RequestBody(required = false)List<BinaryContentCreateDto> binaryContentCreateDtos) {
        Message message = messageService.createMessage(messageCreateDto, binaryContentCreateDtos != null? binaryContentCreateDtos : List.of());
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable UUID messageId) {
        Message message = messageService.findById(messageId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> getMessagesByChannel(@PathVariable UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateDto messageUpdateDto){
        Message updateMessage = messageService.update(messageId, messageUpdateDto);
        return ResponseEntity.ok(updateMessage);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/channel/{channelId}")
    public ResponseEntity<Void> deleteMessagesInChannel(@PathVariable UUID channelId) {
        messageService.deleteInChannel(channelId);
        return ResponseEntity.noContent().build();
    }
}

package com.example.mission.discodeit.controller;

import com.example.mission.discodeit.entity.Message;
import com.example.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        return messageService.sendMessage(message);
    }

    @GetMapping("/channel/{channelId}")
    public List<Message> getMessagesByChannel(@PathVariable Long channelId) {
        return messageService.getMessagesByChannel(channelId);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable Long id, @RequestBody String content) {
        return messageService.updateMessage(id, content);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }
}
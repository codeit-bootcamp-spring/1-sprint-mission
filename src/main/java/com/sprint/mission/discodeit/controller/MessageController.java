package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody CreateMessageRequestDto request){
        Message createdMessage=messageService.createMessage(request);
        return ResponseEntity.ok(createdMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID id, @RequestBody UpdateMessageRequestDto request){
        Message updateMessage=messageService.updateMessage(id,request);
        return ResponseEntity.ok(updateMessage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String > deleteMessage(@PathVariable UUID id){
        messageService.deleteMessage(id);
        return ResponseEntity.ok("Message deleted successfully");
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Message>> getMessagesByChannel(@PathVariable UUID channelId){
        List<Message> messages=messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages=messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }
}

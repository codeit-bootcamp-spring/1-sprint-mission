package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/message")
public class MessageRestController {
    private final MessageService messageService;

    @PostMapping
    public MessageResponse sendMessage(@RequestBody MessageRequest request){
        return messageService.create(request);
    }

    @GetMapping("/list")
    public List<MessageResponse> messageList() { return messageService.readAll(); }

    @GetMapping("/list/{channelId}")
    public List<MessageResponse> channelMessageList(@PathVariable UUID channelId){
        return messageService.channelMessageReadAll(channelId);
    }

    @PutMapping("/{id}")
    public MessageResponse updateMessage(@PathVariable UUID id,
                                         @RequestBody MessageRequest request){
        return messageService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteMessage(@PathVariable UUID id){
        return messageService.delete(id);
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public MessageResponse createMessage(
            @RequestPart MessageRequest.Create messageRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
            ) {
        return messageService.createMessage(messageRequest, files);
    }

    @PutMapping("/{messageId}")
    public MessageResponse updateMessage(
            @PathVariable UUID messageId,
            @RequestPart MessageRequest.Update messageRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return messageService.update(messageId, messageRequest, files);
    }

    @DeleteMapping("/{messageId}")
    public String deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteById(messageId);
        return "delete ok";
    }

    @GetMapping
    public List<MessageResponse> getMessageListByChannel(@RequestParam("channelId") UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }
}

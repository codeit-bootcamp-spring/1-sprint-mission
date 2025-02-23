package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> createMessage(@RequestBody MessageCreateRequest request) {
        Message message = messageService.create(request, List.of());
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequest request) {
        Message updatedMessage = messageService.update(messageId, request);
        return ResponseEntity.ok(updatedMessage);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessagesForChannel(@PathVariable UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messages);
    }
}

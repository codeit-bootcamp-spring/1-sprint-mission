package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> findByChannel(
            @PathVariable(name = "id") UUID channelId
    ) {
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createMessage(
            @RequestBody MessageCreateRequest messageCreateRequest,
            @RequestBody(required = false) List<BinaryContentCreateRequest> binaryContentCreateRequest
    ) {
        messageService.create(messageCreateRequest, binaryContentCreateRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateMessage(
            @PathVariable(name = "id") UUID messageId,
            @RequestBody MessageUpdateRequest messageUpdateRequest
    ) {
        messageService.update(messageId, messageUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(
            @PathVariable(name = "id") UUID messageId
    ) {
        messageService.delete(messageId);
        return ResponseEntity.ok().build();
    }
}

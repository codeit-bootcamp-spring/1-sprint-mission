package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
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
    private final MessageService messageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageCreateRequest request) {
        Message message = messageService.create(request, List.of());
        return ResponseEntity.ok(new MessageDTO(
                message.getId(),
                message.getChannelId(),
                message.getWriterId(),
                message.getContent(),
                message.getAttachmentIds()
        ));
    }

    @RequestMapping(value = "/update/{messageId}/{writerId}/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<MessageDTO> updateMessage(
            @PathVariable UUID messageId,
            @PathVariable UUID writerId,
            @PathVariable UUID channelId,
            @RequestBody MessageUpdateRequest request
    ) {
        Message message = messageService.update(messageId, channelId, writerId, request);
        return ResponseEntity.ok(new MessageDTO(
                message.getId(),
                message.getChannelId(),
                message.getWriterId(),
                message.getContent(),
                message.getAttachmentIds()
        ));
    }

    @RequestMapping(value = "/delete/{messageId}/{writerId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(
            @PathVariable UUID messageId,
            @PathVariable UUID writerId) {
        messageService.delete(messageId, writerId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageDTO>> getChannelMessages(@PathVariable UUID channelId) {
        return ResponseEntity.ok(messageService.findByChannel(channelId));
    }
}

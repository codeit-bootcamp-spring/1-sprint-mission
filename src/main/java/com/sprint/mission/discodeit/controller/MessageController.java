package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateResponse;
import com.sprint.mission.discodeit.dto.message.MessageFindBResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageCreateResponse> createMessage(@RequestBody MessageCreateRequest messageCreateRequest,
                                                               @RequestBody List<BinaryContentCreateRequest> binaryContentCreateRequests){
        return ResponseEntity.ok(messageService.createMessage(messageCreateRequest, binaryContentCreateRequests));
    }

    @RequestMapping(value ="/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateRequest messageUpdateRequest){
        messageService.updateMessageText(messageId, messageUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId){
        messageService.deleteMessageById(messageId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<Collection<MessageFindBResponse>> getMessageByChannel(@PathVariable UUID channelId){
        return ResponseEntity.ok(messageService.findAllByChannelId(channelId));
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.wrapper.MessageCreateWrapper;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> create(@RequestBody MessageCreateWrapper messageCreateWrapper) {
        return ResponseEntity.ok(messageService.create(messageCreateWrapper.messageCreateRequest(),
                messageCreateWrapper.binaryContentRequests()));
    }

    @RequestMapping(value = "/channel/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getAllMessagesByChannelId(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.findAllByChannelId(id));
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getAllMessagesByUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.findAllByAuthorId(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Message> update(@PathVariable UUID id, @RequestBody MessageUpdateRequest messageUpdateRequest) {
        return ResponseEntity.ok(messageService.update(id, messageUpdateRequest));
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageListResponse;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.FileConverter;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final FileConverter fileConverter;
    private final BasicMessageService messageService;

    @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<MessageResponse> create(
            @RequestPart("message") MessageCreateRequest request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        List<BinaryContentCreateRequest> binaryContentRequest = attachments.stream()
                .map(fileConverter::convertToBinaryRequest)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Message message = messageService.create(request, binaryContentRequest);

        MessageResponse response = MessageResponse.from(message);
        if (binaryContentRequest.isEmpty() && !attachments.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse> update(
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateRequest request
    ) {
        try {
            Message message = messageService.update(messageId, request);
            MessageResponse response = MessageResponse.from(message);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse> find(
            @PathVariable UUID messageId
    ){
        Message message = messageService.find(messageId);
        MessageResponse response = MessageResponse.from(message);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<MessageListResponse> findMessagesByChannel(
            @PathVariable UUID channelId
    ){
        List<Message> messages = messageService.findAllByChannelId(channelId);

        MessageListResponse response = MessageListResponse.from(messages);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(
            @PathVariable UUID messageId
    ){
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

}
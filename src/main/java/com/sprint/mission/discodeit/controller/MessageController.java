package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MessageDTO> createMessage(
            @RequestPart("MessageCreateRequest") MessageCreateRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
                .map(files -> files.stream()
                        .map(file -> {
                            try {
                                return new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList())
                .orElse(new ArrayList<>());
        Message message = messageService.create(messageCreateRequest, attachmentRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(message));
    }

    @RequestMapping(value = "/update/{messageId}/{writerId}/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<MessageDTO> updateMessage(
            @PathVariable UUID messageId,
            @PathVariable UUID writerId,
            @PathVariable UUID channelId,
            @RequestBody MessageUpdateRequest request
    ) {
        Message message = messageService.update(messageId, channelId, writerId, request);
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(message));
    }

    @RequestMapping(value = "/delete/{messageId}/{writerId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(
            @PathVariable UUID messageId,
            @PathVariable UUID writerId) {
        messageService.delete(messageId, writerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageDTO>> getChannelMessages(@PathVariable UUID channelId) {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.findByChannel(channelId));
    }

    private MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getChannelId(),
                message.getWriterId(),
                message.getContent(),
                message.getAttachmentIds()
        );
    }

}

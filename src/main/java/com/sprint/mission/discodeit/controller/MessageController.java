package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;

    public List<BinaryContentCreateRequest> resolveAttachmentFile(List<MultipartFile> attachmentFile) {
        return attachmentFile.stream()
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
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(@RequestPart("messageCreateRequest")MessageCreateRequest messageCreateRequest, @RequestPart(value = "attachmentFiles", required = false)List<MultipartFile> attachmentFiles) {
        List<BinaryContentCreateRequest> attachments = Optional.ofNullable(attachmentFiles)
                .map(this::resolveAttachmentFile)
                .orElse(List.of());

        Message createdMessage = messageService.createMessage(messageCreateRequest, attachments);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMessage);
    }


    @RequestMapping(path = "update")
    public ResponseEntity<Message> update(@RequestParam("messageId") UUID messageId, @RequestBody MessageUpdateRequest request) {
        Message updatedMessage = messageService.updateMessageField(messageId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedMessage);
    }

    @RequestMapping(path = "delete")
    public ResponseEntity<Void> delete(@RequestParam("messageId") UUID messageId) {
        messageService.deleteMessageById(messageId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @RequestMapping("findAllByChannelId")
    public ResponseEntity<List<Message>> findAllByChannelId(@RequestParam("channelId") UUID channelId) {
        List<Message> messages = messageService.readAllByChannelId(channelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messages);
    }
}


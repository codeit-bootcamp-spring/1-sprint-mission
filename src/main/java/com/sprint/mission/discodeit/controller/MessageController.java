package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageAPI;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
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
public class MessageController implements MessageAPI {
    private MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createMessage(@RequestPart("messageCreateDto") MessageCreateDto messageCreateDto, @RequestPart(value = "attachments", required = false)List<MultipartFile> attachments) {
        List<BinaryContentCreateDto> attachment = Optional.ofNullable(attachments)
                .map(files -> files.stream()
                        .map(file -> {
                            try {
                                return new BinaryContentCreateDto( file.getOriginalFilename(), file.getContentType(), file.getBytes());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).toList())
                .orElse(new ArrayList<>());
        Message createdMessage = messageService.createMessage(messageCreateDto, attachment);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMessage);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessagesByChannel(@RequestParam UUID channelId) {
        List<Message> messages = messageService.findAllByChannelId(channelId);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateDto messageUpdateDto){
        Message updateMessage = messageService.update(messageId, messageUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateMessage);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

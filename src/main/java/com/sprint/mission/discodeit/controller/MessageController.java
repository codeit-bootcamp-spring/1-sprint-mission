package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Message> create(@RequestPart("messageCreateRequest")MessageCreateRequest messageCreateRequest,
        @RequestPart(required = false) List<MultipartFile> multipartFiles) {
        List<BinaryContentRequest> binaryContentRequests = Optional.ofNullable(multipartFiles)
            .map(files -> files.stream()
                .map(file -> {
                    try {
                        return new BinaryContentRequest(
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

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(messageService.create(messageCreateRequest, binaryContentRequests));
    }

    @GetMapping("/channels/{id}")
    public ResponseEntity<List<Message>> getAllMessagesByChannelId(@PathVariable UUID id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.findAllByChannelId(id));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<Message>> getAllMessagesByUserId(@PathVariable UUID id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.findAllByAuthorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> update(@PathVariable UUID id, @RequestBody MessageUpdateRequest messageUpdateRequest) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.update(id, messageUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}

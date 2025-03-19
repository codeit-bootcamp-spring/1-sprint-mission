package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MessageDto> create(@Valid @RequestPart("messageCreateRequest")MessageCreateRequest messageCreateRequest,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(messageService.create(messageCreateRequest, attachments));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageDto> update(@PathVariable UUID id, @RequestBody MessageUpdateRequest request) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        messageService.delete(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping()
    public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
        @RequestParam("channelId") UUID channelId,
        @RequestParam(value = "cursor", required = false) Instant cursor,
        @PageableDefault(
            size = 50,
            page = 0,
            sort = "createdAt",
            direction = Direction.DESC
        )Pageable pageable) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.findAllByChannelId(channelId, cursor, pageable));
    }
}

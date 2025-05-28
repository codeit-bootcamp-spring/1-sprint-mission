package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.docs.MessageApiDocs;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.request.MessageRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageApiDocs {

    private final MessageService messageService;

    @Override
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MessageResponse> createMessage(
        @Valid @RequestPart(value = "messageCreateRequest") MessageRequest.Create messageRequest,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> files
    ) {
        log.info("POST /api/messages");
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(messageService.createMessage(messageRequest, files));
    }

    @Override
    @PatchMapping(value = "/{messageId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MessageResponse> updateMessage(
        @PathVariable UUID messageId,
        @Valid @RequestBody MessageRequest.Update messageRequest
    ) {
        log.info("PUT /api/messages/{}", messageId);
        return ResponseEntity.ok(messageService.update(messageId, messageRequest));
    }

    @Override
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {

        messageService.deleteById(messageId);
        log.info("DELETE /api/messages/{}", messageId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<MessageResponse>> getMessageListByChannel(
        @RequestParam("channelId") UUID channelId,
        @RequestParam(value = "cursor", required = false) Instant cursor,
        @PageableDefault(
            size = 50,
            page = 0,
            sort = "createdAt",
            direction = Direction.DESC
        ) Pageable pageable) {
        log.info("GET /api/messages/{} : channelId={}, cursor={}, pageable={}",
            channelId, channelId, cursor, pageable);
        return ResponseEntity.ok((messageService.findAllByChannelId(channelId, cursor, pageable)));
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("api/messages")
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MessageDto> createMessage(
        @RequestPart(value = "messageCreateRequest") CreateMessageRequest createMessageRequest,
        @RequestPart(value = "attachments", required = false) List<MultipartFile> multipartFileList) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                messageService.createMessage(createMessageRequest, multipartFileList));
    }

    @PreAuthorize("#id == authentication.principal.user.id")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<MessageDto> updateMessage(
        @PathVariable UUID id,
        @RequestBody UpdateMessageRequest updateMessageRequest,
        @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(messageService.updateMessage(id, updateMessageRequest, userDetails));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteMessage(
        @PathVariable UUID id,
        @AuthenticationPrincipal UserDetails userDetails) {
        messageService.deleteMessage(id, userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "")
    public ResponseEntity<PageResponse<MessageDto>> findMessagesByChannel(
        @RequestParam("channelId") UUID channelId,
        @RequestParam(value = "cursor", required = false) Instant cursor,
        @PageableDefault(size = 50, sort = {
            "createdAt"}, direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(messageService.findAllMessagesByChannelId(channelId, cursor, pageable));
    }
}

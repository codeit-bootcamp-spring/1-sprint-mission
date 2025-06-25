package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.MessageApiDocs;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.response.PageResponse;
import com.spirnt.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController implements MessageApiDocs {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 메시지 전송
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> createMessage(
        @Valid @RequestPart MessageCreateRequest messageCreateRequest,
        @RequestPart(required = false) List<MultipartFile> attachments) {
        log.info("[Creating Message started]");
        MessageDto message = messageService.create(messageCreateRequest, attachments);
        log.info("[Message Created / id:{}]", message.getId());

        // 구독 중인 클라이언트에게 전송
        String destination = "/sub/channels." + messageCreateRequest.channelId() + ".messages";
        simpMessagingTemplate.convertAndSend(destination, message);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(message);
    }

    // 메시지 수정
    @PreAuthorize("@authorizationEvaluator.isMessageAuthor(#messageId, authentication.principal.user.id)")
    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
        @Valid @RequestBody MessageUpdateRequest messageUpdateRequest) {
        log.info("[Updating message / id: {}]", messageId);
        MessageDto message = messageService.update(messageId, messageUpdateRequest);
        log.info("[Message Updated / id: {}]", messageId);
        return ResponseEntity.ok(message);
    }

    // 메시지 삭제
    @PreAuthorize("hasRole('ADMIN') or @authorizationEvaluator.isMessageAuthor(#messageId, authentication.principal.user.id)")

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        log.info("[Deleting Message / id: {}]", messageId);
        messageService.delete(messageId);
        log.info("[Message Deleted / id:{}]", messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    // 특정 채널의 메시지 목록 조회
    @GetMapping("")
    public ResponseEntity<PageResponse<MessageDto>> getAllMessagesByChannel(
        @RequestParam UUID channelId,
        @RequestParam(required = false) Instant cursor,
        @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable) {
        PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
            pageable);
        return ResponseEntity.ok(messages);
    }
}

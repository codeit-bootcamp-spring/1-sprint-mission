package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageCreateResponse;
import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    // 메시지 전송
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> createMessage(@RequestParam String content,
                                           @RequestParam UUID userId,
                                           @RequestParam UUID channelId,
                                           @RequestParam(required = false) List<MultipartFile> files) {
        MessageCreateRequest request = new MessageCreateRequest(userId, channelId, content, files);
        Message message = messageService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageCreateResponse(message.getId(), message.getContent()));
    }

    // 메시지 수정
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateMessage(@PathVariable UUID messageId,
                                           @RequestBody String content) {
        MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(content);
        messageService.update(messageId, messageUpdateRequest);
        return ResponseEntity.ok().build();
    }

    // 메시지 삭제
    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    // 특정 채널의 메시지 목록 조회
    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMessagesByChannel(@PathVariable UUID channelId, @RequestParam UUID userId) {
        List<MessageResponse> messageResponses = messageService.findAllByChannelId(channelId, userId);
        return ResponseEntity.ok(messageResponses);
    }
}

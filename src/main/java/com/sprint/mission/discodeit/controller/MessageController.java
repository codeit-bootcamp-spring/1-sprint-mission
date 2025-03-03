package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<MessageResponse> sendMessage(
            @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

        log.info("📩 메시지 전송 요청 도착! Author: {}, Channel: {}",
                messageCreateRequest.getAuthorId(), messageCreateRequest.getChannelId());

        if (messageCreateRequest.getAuthorId() == null || messageCreateRequest.getChannelId() == null) {
            log.error("❌ 잘못된 요청: authorId 또는 channelId가 없습니다.");
            return ResponseEntity.badRequest().build();
        }

        try {
            // 첨부파일 처리 로직 추가 가능
            MessageResponse createdMessage = messageService.create(messageCreateRequest);
            URI location = URI.create("/api/messages/" + createdMessage.getId());
            return ResponseEntity.created(location).body(createdMessage);
        } catch (Exception e) {
            log.error("🚨 메시지 전송 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @PatchMapping으로 변경하여 PATCH 요청을 지원
    @PatchMapping("/{messageId}")
    public ResponseEntity<String> updateMessage(@PathVariable UUID messageId,
                                                @RequestBody MessageUpdateRequest messageUpdateRequest) {
        log.info("✏ 메시지 수정 요청 도착! Message ID: {}", messageId);
        try {
            messageService.update(messageId, messageUpdateRequest);
            return ResponseEntity.ok("✅ 메시지 수정 성공!");
        } catch (Exception e) {
            log.error("🚨 메시지 수정 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ 메시지 수정 실패!");
        }
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID messageId) {
        log.info("🗑 메시지 삭제 요청 도착! Message ID: {}", messageId);
        try {
            messageService.delete(messageId);
            return ResponseEntity.ok("✅ 메시지 삭제 성공!");
        } catch (Exception e) {
            log.error("🚨 메시지 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ 메시지 삭제 실패!");
        }
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByChannel(@PathVariable UUID channelId) {
        log.info("📜 채널 메시지 조회 요청 도착! Channel ID: {}", channelId);
        return ResponseEntity.ok(messageService.readAllByChannel(channelId));
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAllMessages() {
        log.info("📜 모든 메시지 조회 요청 도착!");
        return ResponseEntity.ok(messageService.readAll());
    }
}

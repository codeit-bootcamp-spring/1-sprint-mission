package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateDTO;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // ✅ 1. 메시지 보내기
    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageCreateDTO messageCreateDTO) {
        log.info("📩 메시지 전송 요청 도착! Sender: {}, Channel: {}", messageCreateDTO.getSenderId(), messageCreateDTO.getChannelId());

        if (messageCreateDTO.getSenderId() == null || messageCreateDTO.getChannelId() == null) {
            log.error("❌ 잘못된 요청: senderId 또는 channelId가 없습니다.");
            return ResponseEntity.badRequest().build();
        }

        try {
            MessageDTO createdMessage = messageService.create(messageCreateDTO);
            return ResponseEntity.ok(createdMessage);
        } catch (Exception e) {
            log.error("🚨 메시지 전송 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ 2. 메시지 수정
    @PutMapping("/{messageId}")
    public ResponseEntity<String> updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateDTO messageUpdateDTO) {
        log.info("✏ 메시지 수정 요청 도착! Message ID: {}", messageId);

        try {
            messageService.update(messageId, messageUpdateDTO);
            return ResponseEntity.ok("✅ 메시지 수정 성공!");
        } catch (Exception e) {
            log.error("🚨 메시지 수정 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("❌ 메시지 수정 실패!");
        }
    }

    // ✅ 3. 메시지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID messageId) {
        log.info("🗑 메시지 삭제 요청 도착! Message ID: {}", messageId);

        try {
            messageService.delete(messageId);
            return ResponseEntity.ok("✅ 메시지 삭제 성공!");
        } catch (Exception e) {
            log.error("🚨 메시지 삭제 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("❌ 메시지 삭제 실패!");
        }
    }

    // ✅ 4. 특정 채널의 메시지 목록 조회
    @GetMapping("/{channelId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByChannel(@PathVariable UUID channelId) {
        log.info("📜 채널 메시지 조회 요청 도착! Channel ID: {}", channelId);
        return ResponseEntity.ok(messageService.readAllByChannel(channelId));
    }

    // ✅ 5. 전체 메시지 목록 조회 추가
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        log.info("📜 모든 메시지 조회 요청 도착!");
        return ResponseEntity.ok(messageService.readAll());
    }
}
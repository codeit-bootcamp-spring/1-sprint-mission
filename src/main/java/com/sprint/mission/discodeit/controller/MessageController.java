package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "메시지 생성", description = "메시지 생성 / 공개/비공개 구현x")
    @PostMapping
    public ResponseEntity<MessageDto> create(@Valid @RequestBody MessageDto messageDTO) {
        try {
            MessageDto message = messageService.createMessage(messageDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            log.error("메시지 생성 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "메시지 목록 조회", description = "전체 메시지 조회")
    @GetMapping
    public ResponseEntity<List<MessageDto>> channelMessages() {
        List<MessageDto> messages = messageService.findAll();
        return ResponseEntity.ok(messages);

    }

    @Operation(summary = "메시지 삭제", description = "메시지 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "메시지 수정", description = "메시지 수정")
    @PatchMapping("/{id}")
    public ResponseEntity<MessageDto> updateMessage(
            @PathVariable String id,
            @Valid @RequestBody MessageDto messageDto) {
        try {
            MessageDto updateMessage = messageService.updateMessage(id, messageDto);
            return ResponseEntity.ok(updateMessage);
        }
        catch (Exception e) {
            log.error("메시지 수정 중 오류 발생 : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.wrapper.MessageCreateRequestWrapper;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    // 메시지 전송
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageCreateRequestWrapper messageCreateRequestWrapper) {
        MessageCreateRequest messageCreateRequest = messageCreateRequestWrapper.messageCreateRequest();
        List<BinaryContentCreateRequest> binaryContentCreateRequest = messageCreateRequestWrapper.binaryContentCreateRequestList();
        MessageDto messageDto = messageService.create(messageCreateRequest, binaryContentCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    }

    // 메시지 수정
    @PutMapping("/{messageId}")
    public ResponseEntity<MessageDto> updateMessage(@PathVariable UUID messageId,
                                                    @RequestBody MessageUpdateRequest messageUpdateRequest) {
        MessageDto messageDto = messageService.update(messageId, messageUpdateRequest);
        return ResponseEntity.ok(messageDto);
    }

    // 메시지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    // 특정 채널의 메시지 조회
    @GetMapping("/channels/{channelId}")
    public ResponseEntity<List<MessageDto>> searchAllMessagesByChannelId(@PathVariable UUID channelId) {
        List<MessageDto> messageDtoList = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messageDtoList);
    }
}

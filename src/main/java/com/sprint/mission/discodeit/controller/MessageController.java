package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.FindMessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<FindMessageResponseDto> createMessage(@RequestBody CreateMessageRequestDto createMessageRequestDto) throws IOException {
        FindMessageResponseDto findMessageResponseDto = messageService.create(createMessageRequestDto);

        return ResponseEntity.created(URI.create("/api/message/" + findMessageResponseDto.getId())).body(findMessageResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FindMessageResponseDto> updateMessage(@PathVariable UUID id,
                                                                @RequestParam String context) {
        UpdateMessageRequestDto updateMessageRequestDto = new UpdateMessageRequestDto(id, context);
        FindMessageResponseDto findMessageResponseDto = messageService.updateContext(updateMessageRequestDto);

        return ResponseEntity.ok(findMessageResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FindMessageResponseDto>> findMessage(@PathVariable UUID userId) {
        List<FindMessageResponseDto> findMessages = messageService.findAllByUserId(userId);

        return ResponseEntity.ok(findMessages);
    }
}

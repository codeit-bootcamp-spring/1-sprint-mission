package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.FindMessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<FindMessageResponseDto> createMessage(@RequestParam UUID channelId,
                                                                @RequestParam UUID writerId,
                                                                @RequestParam String context,
                                                                @RequestParam List<MultipartFile> images) throws IOException {
        CreateMessageRequestDto createMessageRequestDto = new CreateMessageRequestDto(channelId, writerId, context, images);
        FindMessageResponseDto findMessageResponseDto = messageService.create(createMessageRequestDto);

        return ResponseEntity.created(URI.create("/api/message/" + findMessageResponseDto.getId())).body(findMessageResponseDto);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<FindMessageResponseDto> updateMessage(@PathVariable UUID id,
                                                                @RequestParam String context) {
        UpdateMessageRequestDto updateMessageRequestDto = new UpdateMessageRequestDto(id, context);
        FindMessageResponseDto findMessageResponseDto = messageService.updateContext(updateMessageRequestDto);

        return ResponseEntity.ok(findMessageResponseDto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public ResponseEntity<List<FindMessageResponseDto>> findMessage(@PathVariable UUID userId) {
        List<FindMessageResponseDto> findMessages = messageService.findAllByUserId(userId);

        return ResponseEntity.ok(findMessages);
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.docs.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageControllerDocs {
    private final MessageService messageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CustomApiResponse<MessageResponseDto> createMessage(
            @RequestPart("message") MessageRequestDto messageReqDto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) throws IOException {
        return CustomApiResponse.created(messageService.create(messageReqDto, attachments));
    }

    @PutMapping(path = "/{messageId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CustomApiResponse<MessageResponseDto> updateMessage(@PathVariable UUID messageId,
                                                               @RequestPart("content") String content,
                                                               @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) throws IOException {
        return CustomApiResponse.created(messageService.update(messageId, content, attachments));
    }

    @DeleteMapping("/{messageId}")
    public CustomApiResponse<MessageResponseDto> deleteMessage(@PathVariable UUID messageId) {
        return CustomApiResponse.created(messageService.delete(messageId));
    }

    @GetMapping
    public CustomApiResponse<PageResponse<MessageResponseDto>> getMessagesByChannel(
            @RequestParam UUID channelId,
            @RequestParam(required = false) UUID cursor
    ) {
        return CustomApiResponse.ok(messageService.findMessagesByChannelId(channelId, cursor));
    }
}

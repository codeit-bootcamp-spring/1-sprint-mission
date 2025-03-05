package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.CustomApiResponse;
import com.sprint.mission.discodeit.docs.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageControllerDocs {
    private final MessageService messageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CustomApiResponse<CommonDTO.idResponse> createMessage(
            @RequestPart("message") MessageDTO.request messageReqDto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        return CustomApiResponse.created(messageService.create(messageReqDto, attachments));
    }

    @PutMapping(path = "/{messageId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CustomApiResponse<CommonDTO.idResponse> updateMessage(@PathVariable Long messageId,
                                                                 @RequestPart("content") String content,
                                                                 @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        return CustomApiResponse.created(messageService.update(messageId, content, attachments));
    }

    @DeleteMapping("/{messageId}")
    public CustomApiResponse<CommonDTO.idResponse> deleteMessage(@PathVariable Long messageId) {
        return CustomApiResponse.created(messageService.delete(messageId));
    }

    @GetMapping
    public CustomApiResponse<List<MessageDTO.response>> getMessagesByChannel(@RequestParam UUID channelId) {
        return CustomApiResponse.created(messageService.findAllByChannelId(channelId));
    }
}

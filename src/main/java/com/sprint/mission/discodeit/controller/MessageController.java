package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.ApiResponse;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<MessageDTO.idResponse> createMessage(
            @RequestPart("message") MessageDTO.request messageReqDto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        return ApiResponse.created(messageService.create(messageReqDto, attachments));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<MessageDTO.idResponse> updateMessage(@PathVariable Long id,
                                                            @RequestPart("content") String content,
                                                            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

        return ApiResponse.created(messageService.update(MessageDTO.update.builder()
                .id(id)
                .content(content)
                .attachments(attachments)
                .build()
        ));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiResponse<MessageDTO.idResponse> deleteMessage(@PathVariable Long id) {
        return ApiResponse.created(messageService.delete(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse<List<MessageDTO.response>> getMessagesByChannel(@RequestParam UUID channelId) {
        return ApiResponse.created(messageService.findAllByChannelId(channelId));
    }
}

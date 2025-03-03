package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/messages")
@Tag(name = "Message API" , description = "메시지 관리 API")
public class MessageRestController {
    private final MessageService messageService;

    @Operation(summary = "message send", description = "메시지 보내기")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public MessageResponse sendMessage(@RequestBody MessageRequest request){
        return messageService.create(request);
    }

    @Operation(summary = "message list", description = "메시지 리스트")
    @GetMapping
    public List<MessageResponse> messageList() { return messageService.readAll(); }

    @Operation(summary = "message - channel list", description = "채널관련 메시지 리스트")
    @GetMapping("/{id}")
    public List<MessageResponse> channelMessageList(@PathVariable UUID id){
        return messageService.channelMessageReadAll(id);
    }

    @Operation(summary = "message update", description = "메시지 업데이트")
    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public MessageResponse updateMessage(@PathVariable UUID id,
                                         @RequestBody MessageRequest request){
        return messageService.update(id, request);
    }

    @Operation(summary = "message delete", description = "메시지 삭제")
    @DeleteMapping("/{id}")
    public boolean deleteMessage(@PathVariable UUID id){
        return messageService.delete(id);
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResultDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/message")
    public ResultDTO<UUID> sendMessage(@RequestBody MessageCreateDTO request){
        return ResultDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("메시지가 생성 완료")
                .data(messageService.create(request))
                .build();
    }

    @PutMapping("/messages/{messageId}")
    public ResultDTO updateMessage(@PathVariable UUID messageId, @RequestBody MessageUpdateDTO request){
        messageService.update(messageId, request);
        return ResultDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("메시지 수정 완료")
                .build();
    }

    @DeleteMapping("/messages/{messageId}")
    public ResultDTO<UUID> deleteMessage(@PathVariable UUID messageId){
        return ResultDTO.<UUID>builder()
                .code(HttpStatus.OK.value())
                .message("메시지가 생성 완료")
                .data(messageService.delete(messageId))
                .build();
    }

    @GetMapping("/messages/{channelId}")
    public ResultDTO<List<Message>> getMessageByChannel(@PathVariable UUID channelId){
        return ResultDTO.<List<Message>>builder()
                .code(HttpStatus.OK.value())
                .message("메시지가 생성 완료")
                .data(messageService.findAllByChannelId(channelId))
                .build();
    }



}

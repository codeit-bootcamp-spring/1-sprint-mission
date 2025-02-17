package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //메시지 생성
    @PostMapping
    public MessageResponseDTO createMessage(@RequestBody MessageCreateDTO messageCreateDTO) {
        return messageService.create(messageCreateDTO);
    }

    //메세지 수정
    @PatchMapping("/modify")
    public MessageResponseDTO modifyMessage(@RequestBody MessageUpdateDTO messageUpdateDTO) {
        return messageService.update(messageUpdateDTO);
    }

    //메세지 삭제
    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable UUID messageId) {
        messageService.delete(messageId);
    }

    //특정채널의 메시지 목록 조회
    @GetMapping("/{channelId}")
    public List<MessageResponseDTO> getListMessages(@PathVariable UUID channelId) {
        return messageService.findMessagesByChannelId(channelId);
    }

}

package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDTO;
import com.sprint.mission.discodeit.service.interfacepac.MessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //메시지 생성
    @PostMapping(consumes ={MediaType.MULTIPART_FORM_DATA_VALUE})
    public MessageResponseDTO createMessage(
            @RequestPart("message-create-dto") MessageCreateDTO messageCreateDTO,
            @RequestPart("binary-content-create-request") MultipartFile binaryContentCreateRequest
    ) {
        BinaryContentCreateRequest binaryContent = null;
        if (Objects.nonNull(binaryContentCreateRequest)) {
            try {
                binaryContent = new BinaryContentCreateRequest(
                        binaryContentCreateRequest.getOriginalFilename(),
                        binaryContentCreateRequest.getContentType(),
                        binaryContentCreateRequest.getBytes()
                );
            } catch (IOException exception) {
                throw new IllegalArgumentException(exception);
            }
        }
        return messageService.create(messageCreateDTO , binaryContent);
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

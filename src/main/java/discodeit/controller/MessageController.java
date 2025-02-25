package discodeit.controller;

import discodeit.dto.binarycontent.AddBinaryContentRequest;
import discodeit.dto.message.CreateMessageRequest;
import discodeit.dto.message.MessageDto;
import discodeit.dto.message.UpdateMessageRequest;
import discodeit.entity.Message;
import discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageDto> createMessage(@RequestBody CreateMessageRequest request) {
        Message message = messageService.create(request);
        return ResponseEntity.ok(MessageDto.fromDomain(message));
    }

    @RequestMapping(value = "{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<MessageDto>> getAllMessageByChannelId(@PathVariable("channelId") UUID channelId) {
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        List<MessageDto> messageListDto = messageList.stream().map(MessageDto::fromDomain).toList();
        return ResponseEntity.ok(messageListDto);
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<MessageDto> updateMessage(@RequestBody UpdateMessageRequest request) {
        Message message = messageService.update(request);
        return ResponseEntity.ok(MessageDto.fromDomain(message));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteMessage(@RequestParam("id") UUID messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok("매세지 ID: " + messageId + " delete Complete!!");
    }

}

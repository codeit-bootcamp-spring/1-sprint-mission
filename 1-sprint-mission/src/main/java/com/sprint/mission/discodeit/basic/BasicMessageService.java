package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.DTO.message.CreateMessageBinaryContentRequest;
import com.sprint.mission.discodeit.DTO.message.CreateMessageImageRequest;
import com.sprint.mission.discodeit.DTO.NameIdDto;
import com.sprint.mission.discodeit.DTO.NameNameDto;
import com.sprint.mission.discodeit.DTO.message.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.etc.parse;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class BasicMessageService extends parse {
    private final MessageService messageService;




    public BasicMessageService(@Qualifier("FileMessageService") MessageService messageService){
        this.messageService= messageService;
    }

    @PostMapping("/normal")
    public void createNewMessage(@RequestBody NameNameDto nameNameDto) {
        String title=nameNameDto.getName1();
        String change=nameNameDto.getName2();
        messageService.createNewMessage(title,change);
    }

    @PostMapping("/binaryContent")
    public void createNewMessage(@RequestBody CreateMessageBinaryContentRequest createMessageBinaryContentRequest) {
        String title=createMessageBinaryContentRequest.getTitle();
        String body=createMessageBinaryContentRequest.getBody();
        List<BinaryContent> binaryContents=createMessageBinaryContentRequest.getBinaryContents();
        messageService.createNewMessage(title,body,binaryContents);
    }

    @PostMapping("/img")
    public void createNewMessagetoImg(@RequestBody CreateMessageImageRequest createMessageImageRequest) {

        String title=createMessageImageRequest.getTitle();
        String body=createMessageImageRequest.getBody();
        List<char []> imgs=createMessageImageRequest.getImgs();

        messageService.createNewMessagetoImg(title,body,imgs);
    }

    @GetMapping("/id/{key}")
    public  List<MessageDto> readMessage(@PathVariable  UUID key) {
        return MessageDto.fromEntity(messageService.readMessage(key));
    }

    @GetMapping("/title/{key}")
    public  List<MessageDto> readMessage(@PathVariable  String key) {
        return  MessageDto.fromEntity(messageService.readMessage(key));
    }

    @GetMapping("/all")
    public List<MessageDto> readMessageAll() {
        return MessageDto.fromEntity(messageService.readMessageAll());
    }

    @PatchMapping("/message-title/id")
    public boolean updateMessageTitle(@RequestBody NameIdDto nameIdDto) {
        UUID id=nameIdDto.getId();
        String change=nameIdDto.getName();
        return messageService.updateMessageTitle(id,change);
    }

    @PatchMapping("/message-title/title")
    public boolean updateMessageTitle(@RequestBody NameNameDto nameNameDto) {
        String name=nameNameDto.getName1();
        String change=nameNameDto.getName2();
        return messageService.updateMessageBody(name,change);
    }

    @PatchMapping("/message-body/id")
    public boolean updateMessageBody(@RequestBody NameIdDto nameIdDto) {
        UUID id=nameIdDto.getId();
        String change=nameIdDto.getName();
        return messageService.updateMessageBody(id,change);
    }

    @PatchMapping("/message-body/name")
    public boolean updateMessageBody(@RequestBody NameNameDto nameNameDto) {
        String name=nameNameDto.getName1();
        String change=nameNameDto.getName2();
        return messageService.updateMessageBody(name,change);
    }

    @DeleteMapping("/id")
    public boolean deleteMessage(@RequestBody UUID id) {
        return messageService.deleteMessage(id);
    }

    @DeleteMapping("/title")
    public boolean deleteMessage(@RequestBody String title) {
        return messageService.deleteMessage(title);
    }



}

package some_path._1sprintmission.discodeit.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import some_path._1sprintmission.discodeit.dto.MessageCreateRequestDTO;
import some_path._1sprintmission.discodeit.dto.MessageUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class BasicMessageController {

    private final MessageService messageService;

    public BasicMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("")
    public ResponseEntity createMessage(@RequestBody MessageCreateRequestDTO messageCreateRequestDTO){
        Message newMessage = messageService.create(messageCreateRequestDTO);
        return new ResponseEntity(newMessage, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity updateMessage(@RequestBody MessageUpdateRequestDTO messageUpdateRequestDTO){
        Message updateMessage = messageService.update(messageUpdateRequestDTO);
        return new ResponseEntity(updateMessage,HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity deleteMessage(@RequestBody UUID messageId){
        messageService.delete(messageId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity GetAllMessage(@RequestBody UUID channelId){
        List<Message> messageInChannel = messageService.findAllByChannelId(channelId);
        return new ResponseEntity(messageInChannel, HttpStatus.OK);
    }
}

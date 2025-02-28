package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping
  public String sendMessage(@RequestBody MessageCreateDTO messageCreateDTO) {
    messageService.createMessage(messageCreateDTO);
    return messageCreateDTO.toString();
  }

  @PatchMapping("/{id}")
  public String updateMessage(@PathVariable("id") UUID id, @RequestBody MessageUpdateDTO messageUpdateDTO) {
    messageService.update(messageUpdateDTO);
    return messageUpdateDTO.toString();
  }

  @DeleteMapping("/{id}")
  public String deleteMessage(@PathVariable("id") UUID id) {
    messageService.deleteMessage(id);
    return ("message deleted");
  }

  @GetMapping("/{id}")
  public List<Message> getMessagesByChannelId(@PathVariable("id") UUID id) {
    return messageService.findAllByChannelId(id);
  }

}

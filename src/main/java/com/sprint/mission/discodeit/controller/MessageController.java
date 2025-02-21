package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ChannelService channelService;
    @RequestMapping(value = "/message/create", method = RequestMethod.POST)
    public ResponseEntity<UUID> createMessage(@RequestParam String content, @RequestParam UUID sender, @RequestParam UUID channelId, Model model) {
        UUID messageId = messageService.create(new MessageDto(sender, channelId, content));
        model.addAttribute("messageId", messageId);
        return ResponseEntity.ok(messageId);
    }

    @RequestMapping(value = "/message/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<Message> findMessage(@PathVariable UUID id, Model model) {
        //Message findMessage = messageService.findById(id);
        //model.addAttribute("message", findMessage);
        return ResponseEntity.ok(messageService.findById(id));
    }

    @RequestMapping(value = "/message/messages", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> findMessage(Model model) {
        //List<Message> messages = messageService.findAll();
        //model.addAttribute("messages", messages);
        return ResponseEntity.ok(messageService.findAll());
    }

    @RequestMapping(value = "/message/update", method = RequestMethod.POST)
    public ResponseEntity<Message> updateMessage(@RequestParam UUID messageId, @RequestParam String content, Model model) {
        messageService.update(new MessageDto(messageId, content));
        //Message updatedMessage = messageService.findById(messageId);
        //model.addAttribute("message", updatedMessage);
        return ResponseEntity.ok(messageService.findById(messageId));
    }

    @RequestMapping(value = "/message/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteMessage(@PathVariable UUID id, Model model) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @RequestMapping(value = "message/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<UUID>> channelMessage(@PathVariable UUID channelId, Model model) {
        //List<UUID> messagesId = channelService.findAllMessagesByChannelId(channelId);
        //model.addAttribute("messages", messagesId);//?
        return ResponseEntity.ok(channelService.findAllMessagesByChannelId(channelId));
    }

}

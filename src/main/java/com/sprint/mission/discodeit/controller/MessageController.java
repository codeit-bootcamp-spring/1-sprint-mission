package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ChannelService channelService;
    private final UserService userService;

    @GetMapping("/create")
    public String messageCreateForm(Model model) {
        model.addAttribute("message", new MessageDTO());
        model.addAttribute("channels", channelService.findAll());
        model.addAttribute("users", userService.findAll());
        return "message/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("message") MessageDTO messageDTO) {
        try {
            messageDTO.setChannelName(messageDTO.getChannelName());
            messageDTO.setSenderName(messageDTO.getSenderName());

            log.info("Creating message: channelId={}, senderId={}, content={}",
                    messageDTO.getChannelId(), messageDTO.getSenderId(), messageDTO.getContent());

            messageService.createMessage(messageDTO);

            log.info("Message created successfully");

            return "redirect:/messages/channel/" + messageDTO.getChannelId();
        } catch (Exception e) {
            log.error("메시지 생성 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/messages/create";
        }
    }

    @GetMapping("/channel/{channelId}")
    public String getChannelMessages(@PathVariable String channelId, Model model) {
        try {
            log.info("채널 메시지 조회: channelId={}", channelId);

            List<MessageDTO> messages = messageService.findAllByChannelId(channelId);
            log.info("조회된 메시지 수: {}", messages.size());

            for (MessageDTO msg : messages) {
                log.info("메시지: id={}, content={}, sender={}",
                        msg.getId(), msg.getContent(), msg.getSenderName());
            }

            model.addAttribute("messages", messages);
            model.addAttribute("currentChannelId", channelId);
            model.addAttribute("channels", channelService.findAll());

            return "message/list";
        } catch (Exception e) {
            log.error("메시지 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/";
        }
    }
    @GetMapping("/list")
    public String getAllMessages(Model model) {
        try {
            log.info("모든 메시지 목록 조회 요청");

            List<MessageDTO> messages = messageService.findAll();
            log.info("조회된 전체 메시지 수: {}", messages.size());

            model.addAttribute("messages", messages);

            return "message/list";
        } catch (Exception e) {
            log.error("메시지 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/";
        }
    }

    @GetMapping("/message/list")
    public String messageList(Model model, @RequestParam String channelId) {
        List<MessageDTO> messages;
        if (channelId != null && !channelId.isEmpty()) {
            messages = messageService.findAllByChannelId(channelId);
        } else {
            messages = messageService.findAll();
        }
        model.addAttribute("messages", messages);
        return "message/list";
    }

}
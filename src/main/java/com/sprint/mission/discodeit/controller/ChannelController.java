package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService channelService;

    // 채널 목록
    @GetMapping
    public String listChannels(Model model) {
        List<Channel> channels = channelService.findAll();
        model.addAttribute("channels", channels);
        return "channel-list";
    }

    // 채널 생성 폼
    @GetMapping("/create")
    public String createChannelForm(Model model) {
        model.addAttribute("channel", new ChannelDTO());
        return "channel-create";
    }

    // 채널 생성 처리
    @PostMapping("/create")
    public String createChannelSubmit(@ModelAttribute("channel") ChannelDTO channelDTO) {
        channelService.create(channelDTO);
        return "redirect:/channels";
    }

    // 채널 삭제
    @GetMapping("/delete/{id}")
    public String deleteChannel(@PathVariable String id) {
        channelService.delete(id);
        return "redirect:/channels";
    }
}
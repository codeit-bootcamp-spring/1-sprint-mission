package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelFindAllDto;
import com.sprint.mission.discodeit.dto.ChannelFindDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(value = "/channels/create/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> createPublic(@RequestParam String name) {
        return ResponseEntity.ok(channelService.create(new ChannelDto(name, ChannelType.PUBLIC)));
    }

    @RequestMapping(value = "/channels/create/private", method = RequestMethod.POST)
    public ResponseEntity<ChannelDto> createPrivate(@RequestParam String name) {
        return ResponseEntity.ok(channelService.create(new ChannelDto(name, ChannelType.PRIVATE)));
    }

    @RequestMapping(value = "/channels/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<ChannelFindDto> find(@PathVariable UUID id, Model model) {
        //ChannelFindDto channelFindDto = channelService.find(id);
        //model.addAttribute("channel", channelFindDto);
        return ResponseEntity.ok(channelService.find(id));
    }
    //이거 Pathvariable, requestBody로 받아야하는 기준
    @RequestMapping(value = "/channels/findAll", method = RequestMethod.GET)
    public ResponseEntity<ChannelFindAllDto> findAll(Model model) {
        //ChannelFindAllDto channel = channelService.findAll();
        //model.addAttribute("channel", channel);
        return ResponseEntity.ok(channelService.findAll());
    }
    @RequestMapping(value = "/channels/update/public", method = RequestMethod.POST)
    public ResponseEntity<ChannelFindDto> update(@RequestParam UUID id, @RequestParam String name, Model model) {
        channelService.updateChannel(new ChannelDto(id, name));
        //update된 채널 정보 반환
        //model.addAttribute("channel", channelService.find(id));
        return ResponseEntity.ok(channelService.find(id));
    }
    @RequestMapping(value = "/channels/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }
}

package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channel")
@AllArgsConstructor
public class ChannelRestController {
    private final ChannelService channelService;

    @PostMapping
    public ChannelResponse channelCreate(@RequestBody ChannelRequest request){
        return channelService.create(request);
    }

    @GetMapping("/list")
    public List<ChannelResponse> publicChannelList(){
        return channelService.publicChannelReadAll();
    }
    @GetMapping("/list/all")
    public List<ChannelResponse> channelList(){
        return channelService.readAll();
    }

    @GetMapping("/{id}")
    public ChannelResponse findByChannel(@PathVariable UUID id){
        return channelService.readOne(id);
    }

    @PutMapping("/update/{id}")
    public ChannelResponse updateChannel(@PathVariable UUID id,
                                         @RequestBody ChannelRequest request){
        return channelService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteChannel(@PathVariable UUID id){
        return channelService.delete(id);
    }

}

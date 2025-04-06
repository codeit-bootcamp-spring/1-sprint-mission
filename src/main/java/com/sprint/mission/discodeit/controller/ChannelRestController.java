package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.*;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel API" , description = "채널 관리 API")
@AllArgsConstructor
public class ChannelRestController {
    private final ChannelService channelService;

    @Operation(summary = "channel create", description = "채널 등록")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ChannelResponse channelCreate(@RequestBody @JsonProperty ChannelRequest request){
        log.info("channel create request : {}", request);
        return channelService.create(request);
    }

    @Operation(summary = "channel list", description = "전체 채널 리스트")
    @GetMapping
    public List<ChannelResponse> channelList(){
        log.info("channel list");
        return channelService.readAll();
    }

    @Operation(summary = "channel find", description = "채널 검색")
    @GetMapping("/{id}")
    public ChannelResponse findByChannel(@PathVariable UUID id){
        log.info("channel find request : {}", id);
        return channelService.readOne(id); }

    @Operation(summary = "channel public list", description = "일반 채널 리스트")
    @GetMapping("/public")
    public List<ChannelResponse> publicChannelList(){
        log.info("public channel list");
        return channelService.publicChannelReadAll();
    }

    @Operation(summary = "channel public find", description = "일반 채널 검색")
    @GetMapping("/public/{id}")
    public ChannelResponse findByPublicChannel(@PathVariable UUID id) {
        log.info("public channel find request : {}", id);
        return channelService.publicChannelReadOne(id); }

    @Operation(summary = "channel update", description = "채널 업데이트")
    @PutMapping(path="/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ChannelResponse updateChannel(@PathVariable UUID id,
                                             @RequestBody ChannelRequest request){
        log.info("channel update request : {}", request);
        return channelService.update(id, request);
    }

    @Operation(summary = "channel delete", description = "채널 삭제")
    @DeleteMapping("/{id}")
    public boolean deleteChannel(@PathVariable UUID id){
        log.info("channel delete request : {}", id);
        return channelService.delete(id);
    }

}

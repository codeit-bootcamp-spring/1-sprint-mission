package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    // 채널 목록
    @GetMapping
    public ResponseEntity<List<ChannelDto>> listChannels() {
        List<ChannelDto> channels = channelService.findAll();
        return ResponseEntity.ok(channels);
    }

    // 채널 생성
    @PostMapping
    public ResponseEntity<ChannelDto> createChannel(@Valid @RequestBody ChannelDto channelDTO) {
        ChannelDto channel = channelService.create(channelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    // 채널 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String id) {
        channelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
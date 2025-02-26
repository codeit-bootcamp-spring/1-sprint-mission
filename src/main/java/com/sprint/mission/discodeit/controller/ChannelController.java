package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "채널 조회", description = "전체 채널 조회")
    @GetMapping
    public ResponseEntity<List<ChannelDto>> listChannels() {
        List<ChannelDto> channels = channelService.findAll();
        return ResponseEntity.ok(channels);
    }


    @Operation(summary = "채널 생성", description = "채널 생성 / 공개/비공개 구현x")
    @PostMapping
    public ResponseEntity<ChannelDto> createChannel(@Valid @RequestBody ChannelDto channelDTO) {
        ChannelDto channel = channelService.create(channelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @Operation(summary = "채널 수정", description = "채널 수정")
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(
            @PathVariable String channelId,
            @Valid @RequestBody ChannelDto channelDto) {

        ChannelDto updateChannel = channelService.update(channelId, channelDto);
        return ResponseEntity.ok(updateChannel);
    }

    @Operation(summary = "채널 삭제", description = "채널 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String id) {
        channelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
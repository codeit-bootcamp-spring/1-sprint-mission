package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @Operation(summary = "User가 참여 중인 Channel 목록 조회", description = "사용자 ID로 참여 중인 채널 목록 조회")
    @GetMapping
    public ResponseEntity<List<ChannelDto>> listChannels(@RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }

    @Operation(summary = "Public Channel 생성", description = "공개 채널 생성")
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> createPublicChannel(@Valid @RequestBody PublicChannelCreateRequest request) {
        ChannelDto channel = channelService.createPublicChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }
    
    @Operation(summary = "Private Channel 생성", description = "비공개 채널 생성")
    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createPrivateChannel(@Valid @RequestBody PrivateChannelCreateRequest request) {
        ChannelDto channel = channelService.createPrivateChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @Operation(summary = "Channel 정보 수정", description = "채널 정보 수정")
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(
            @PathVariable UUID channelId,
            @Valid @RequestBody PublicChannelUpdateRequest request) {
        ChannelDto updatedChannel = channelService.updateChannel(channelId, request);
        return ResponseEntity.ok(updatedChannel);
    }

    @Operation(summary = "Channel 삭제", description = "채널 삭제")
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Channel 상세 정보 조회", description = "채널 상세 정보 조회")
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDto> getChannelDetails(@PathVariable UUID channelId) {
        ChannelDto channel = channelService.findById(channelId);
        return ResponseEntity.ok(channel);
    }
}
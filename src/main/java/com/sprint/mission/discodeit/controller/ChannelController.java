package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    // ✅ 채널 생성 (공개/비공개 포함)
    @PostMapping
    public ResponseEntity<ChannelResponse> createChannel(@RequestBody ChannelCreateRequest channelCreateRequest) {
        ChannelResponse createdChannel;

        if (channelCreateRequest.isPrivate()) {
            createdChannel = channelService.createPrivateChannel(channelCreateRequest);
        } else {
            createdChannel = channelService.createPublicChannel(channelCreateRequest);
        }

        return ResponseEntity.created(URI.create("/api/channels/" + createdChannel.getId()))
                .body(createdChannel);
    }

    // ✅ 모든 채널 조회 (GET /api/channels)
    @GetMapping
    public ResponseEntity<List<ChannelResponse>> getAllChannels() {
        return ResponseEntity.ok(channelService.readAll());
    }

    // ✅ 특정 채널 조회 (GET /api/channels/{channelId})
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> getChannelById(@PathVariable UUID channelId) {
        return channelService.read(channelId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ 채널 업데이트 (PATCH /api/channels/{channelId})
    @PatchMapping("/{channelId}")
    public ResponseEntity<Void> update(@PathVariable UUID channelId, @RequestBody ChannelUpdateRequest channelUpdateRequest) {
        channelService.update(channelId, channelUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    // ✅ 채널 삭제 (DELETE /api/channels/{channelId}) - 존재 여부 체크 후 삭제
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }
}

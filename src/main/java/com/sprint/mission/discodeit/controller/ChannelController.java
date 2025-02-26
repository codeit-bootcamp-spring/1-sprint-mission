package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelCreateDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.ChannelUpdateDTO;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    // ✅ 1. 공개 채널 생성
    @PostMapping("/public")
    public ResponseEntity<Void> createPublicChannel(@RequestBody ChannelCreateDTO channelCreateDTO) {
        channelService.createPublicChannel(channelCreateDTO);
        return ResponseEntity.ok().build();
    }

    // ✅ 2. 비공개 채널 생성
    @PostMapping("/private")
    public ResponseEntity<Void> createPrivateChannel(@RequestParam UUID creatorId, @RequestBody List<UUID> members) {
        channelService.createPrivateChannel(creatorId, members);
        return ResponseEntity.ok().build();
    }

    // ✅ 3. 모든 채널 조회
    @GetMapping
    public ResponseEntity<List<ChannelDTO>> getAllChannels() {
        return ResponseEntity.ok(channelService.readAll());
    }

    // ✅ 4. 특정 채널 조회
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelDTO> getChannelById(@PathVariable UUID channelId) {
        return channelService.read(channelId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ 5. 채널 수정
    @PutMapping("/{channelId}")
    public ResponseEntity<Void> updateChannel(@PathVariable UUID channelId, @RequestBody ChannelUpdateDTO channelUpdateDTO) {
        channelService.update(channelId, channelUpdateDTO);
        return ResponseEntity.ok().build();
    }

    // ✅ 6. 채널 삭제
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity.ok().build();
    }

    // ✅ 7. 특정 사용자가 볼 수 있는 채널 목록 조회 (추가된 부분)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelDTO>> getUserChannels(@PathVariable UUID userId) {
        List<ChannelDTO> channels = channelService.getChannelsForUser(userId);
        return ResponseEntity.ok(channels);
    }
}

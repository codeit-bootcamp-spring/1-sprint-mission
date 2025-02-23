package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService channelService;

    /** 🔹 공용 채널 생성 */
    @PostMapping("/public")
    public ResponseEntity<Channel> createPublicChannel(@RequestBody PublicChannelCreateRequest request) {
        Channel createdChannel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    /** 🔹 비공개 채널 생성 */
    @PostMapping("/private")
    public ResponseEntity<Channel> createPrivateChannel(@RequestBody PrivateChannelCreateRequest request) {
        Channel createdChannel = channelService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    /** 🔹 특정 채널 조회 */
    @GetMapping("/{channelId}")
    public ResponseEntity<?> findChannel(@PathVariable UUID channelId) {
        try {
            ChannelDto channelDto = channelService.find(channelId);
            return ResponseEntity.ok(channelDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Channel not found");
        }
    }

    /** 🔹 특정 사용자가 가입한 채널 목록 조회 */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChannelDto>> findUserChannels(@PathVariable UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channels);
    }

    /** 🔹 채널 정보 업데이트 */
    @PutMapping("/{channelId}")
    public ResponseEntity<?> updateChannel(
            @PathVariable UUID channelId,
            @RequestBody PublicChannelUpdateRequest request
    ) {
        try {
            Channel updatedChannel = channelService.update(channelId, request);
            return ResponseEntity.ok(updatedChannel);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Channel not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Private channels cannot be updated");
        }
    }

    /** 🔹 채널 삭제 */
    @DeleteMapping("/{channelId}")
    public ResponseEntity<?> deleteChannel(@PathVariable UUID channelId) {
        try {
            channelService.delete(channelId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Channel not found");
        }
    }
}

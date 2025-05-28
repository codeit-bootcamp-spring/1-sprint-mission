package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.ChannelApiDocs;
import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.spirnt.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelController implements ChannelApiDocs {

    private final ChannelService channelService;

    // 공개 채널 생성
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")  // 최소 ROLE_CHANNEL_MANAGER 권한 필요
    @PostMapping("/public")
    public ResponseEntity<ChannelDto> creatChannelPublic(
        @Valid @RequestBody PublicChannelCreateRequest request) {
        log.info("[Creating Public Chanel started]");
        ChannelDto channelDto = channelService.createChannelPublic(request);
        log.info("[Public Channel Created / id: {}]", channelDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(channelDto);
    }

    // 비공개 채널 생성
    @PostMapping("/private")
    public ResponseEntity<ChannelDto> createChannelPrivate(
        @Valid @RequestBody PrivateChannelCreateRequest request) {
        log.info("[Creating Private Channel started]");
        ChannelDto channelDto = channelService.createChannelPrivate(request);
        log.info("[Private Channel Created / id: {}]", channelDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(channelDto);
    }

    // 공개 채널 정보 수정
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")// 최소 ROLE_CHANNEL_MANAGER 권한 필요
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updatePublicChannel(@PathVariable UUID channelId,
        @Valid @RequestBody PublicChannelUpdateRequest request) {
        log.info("[Updating Public Channel started / id: {}]", channelId);
        ChannelDto channelDto = channelService.update(channelId, request);
        log.info("[Channel Updated / id: {}]", channelId);
        return ResponseEntity.ok(channelDto);
    }

    // 채널 삭제
    @PreAuthorize("hasRole('CHANNEL_MANAGER')") // 최소 ROLE_CHANNEL_MANAGER 권한 필요
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        log.info("[Deleting Channel started / id: {}]", channelId);
        channelService.delete(channelId);
        log.info("[Channel Deleted / id: {}]", channelId);
        return ResponseEntity.noContent().build();
    }


    // 특정 사용자가 볼 수 있는 채널 목록 조회
    @GetMapping("")
    public ResponseEntity<List<ChannelDto>> getAllChannelsByUserId(@RequestParam UUID userId) {
        List<ChannelDto> channelDtos = channelService.findAllByUserId(userId);
        return ResponseEntity.ok(channelDtos);
    }

}

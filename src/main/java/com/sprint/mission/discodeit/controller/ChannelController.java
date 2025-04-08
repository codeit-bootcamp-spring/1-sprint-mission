package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<ChannelDto> create(@Valid @RequestBody PublicChannelCreateRequest request) {
        ChannelDto channel = channelService.create(request);
        log.info("POST /api/channels/public - create channel: id = {}", channel.id());

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(channel);
    }

    @PostMapping("/private")
    public ResponseEntity<ChannelDto> create(@RequestBody PrivateChannelCreateRequest request) {
        ChannelDto channel = channelService.create(request);
        log.info("POST /api/channels/private - create channel: id = {}", channel.id());

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(channel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChannelDto> update(@PathVariable UUID id, @RequestBody PublicChannelUpdateRequest request) {
        ChannelDto channel = channelService.update(id, request);
        log.info("PATCH /api/channels/{id} - update channel: id = {}", channel.id());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        channelService.delete(id);
        log.info("DELETE /api/channels/{id} - update channel: id = {}", id);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping()
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelService.findAllByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelDto> findChannelById(@PathVariable UUID id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelService.find(id));
    }
}

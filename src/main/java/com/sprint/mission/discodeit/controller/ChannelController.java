package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/public")
    public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest publicChannelCreateRequest) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(channelService.create(publicChannelCreateRequest));
    }

    @PostMapping("/private")
    public ResponseEntity<Channel> create(@RequestBody PrivateChannelCreateRequest privateChannelCreateRequest) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(channelService.create(privateChannelCreateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelResponse> getChannelById(@PathVariable UUID id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelService.find(id));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<ChannelResponse>> getChannelsByUserId(@PathVariable UUID id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelService.findAllByUserId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Channel> update(@PathVariable UUID id, @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(channelService.update(id, publicChannelUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        channelService.delete(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}

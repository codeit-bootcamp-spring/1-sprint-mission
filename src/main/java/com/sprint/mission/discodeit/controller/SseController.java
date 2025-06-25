package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.basic.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {
    private final SseEmitterService sseEmitterService;
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@RequestHeader(value = "Last-Event-ID",required = false) String lastEventId) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().toString());
        return sseEmitterService.connect(userId, lastEventId);
    }

}

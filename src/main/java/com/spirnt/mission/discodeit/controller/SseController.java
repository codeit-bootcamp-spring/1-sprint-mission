package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.security.CustomUserDetails;
import com.spirnt.mission.discodeit.sse.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/sse")
public class SseController {

    private final SseEmitterManager emitterManager;

    @GetMapping("")
    public ResponseEntity<SseEmitter> sseEmitter(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {
        SseEmitter sseEmitter = emitterManager.subscribe(customUserDetails.getUser().getId(),
            lastEventId);
        return ResponseEntity.ok(sseEmitter);
    }

}

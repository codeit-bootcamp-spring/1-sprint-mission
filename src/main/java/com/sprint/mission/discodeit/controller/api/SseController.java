package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {
    
    private final SseService sseService;

    // sse 연결
    @GetMapping
    public SseEmitter createConnection(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(name = "lastEventId", required = false) String lastEventId
    ) {
        return sseService.createConnection(userDetails.getId(), lastEventId);
    }
}

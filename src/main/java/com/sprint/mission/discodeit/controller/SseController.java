package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.SshService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class SseController {

  private final SshService sshService;

  @GetMapping("/sse")
  public SseEmitter subscribe(@RequestParam(value = "userId") UUID userId,
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {
    return sshService.subscribe(
        userId,
        lastEventId != null && !lastEventId.isEmpty() ? UUID.fromString(lastEventId) : null);
  }

}

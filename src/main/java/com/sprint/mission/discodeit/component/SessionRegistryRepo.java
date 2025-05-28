package com.sprint.mission.discodeit.component;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SessionRegistryRepo {
  private final Map<UUID, HttpSession> userSessionMap = new ConcurrentHashMap<>();

  public void register(UUID userId, HttpSession session) {
    userSessionMap.put(userId, session);
  }

  public void invalidate(UUID userId) {
    HttpSession session = userSessionMap.get(userId);
    if (session != null) {
      session.invalidate();
      userSessionMap.remove(userId);
    }
  }
}

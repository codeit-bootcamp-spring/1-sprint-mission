package com.sprint.mission.discodeit.auth;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        event.getSecurityContexts().forEach(securityContext -> {
            String username = securityContext.getAuthentication().getName();
            System.out.println("[INFO] 세션 종료됨: " + username);
        });
    }
}

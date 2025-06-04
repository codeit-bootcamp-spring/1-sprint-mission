package com.sprint.mission.discodeit.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionInspector {

    private final SessionRegistry sessionRegistry;

    public void printActiveSessions() {
        List<Object> principals = sessionRegistry.getAllPrincipals();

        for (Object principal : principals) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            log.info("User: {}, Active Sessions: {}", principal, sessions.size());

            for (SessionInformation session : sessions) {
                log.info("Session ID: {}, Last Request: {}, Expired: {}",
                        session.getSessionId(),
                        session.getLastRequest(),
                        session.isExpired());
            }
        }
    }
}

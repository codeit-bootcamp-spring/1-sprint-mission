package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.auth.CustomUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final SessionRegistry sessionRegistry;

    public boolean isUserLoggedIn(UUID userId) {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(CustomUserDetails.class::isInstance)
                .map(CustomUserDetails.class::cast)
                .anyMatch(user -> user.getUser().getId().equals(userId));
    }
}

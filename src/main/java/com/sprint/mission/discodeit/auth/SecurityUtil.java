package com.sprint.mission.discodeit.auth;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("인증된 사용자가 없습니다.");
        }
        return (CustomUserDetails) auth.getPrincipal();
    }

    public static UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static boolean isAdmin() {
        return getCurrentUser().isAdmin();
    }
}

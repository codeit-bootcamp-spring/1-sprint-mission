package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.repository.jpa.MessageRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccessManager {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    public boolean isSelfOrAdmin(UUID targetUserId, Authentication authentication) {
        // 현재 사용자 ID
        UUID currentUserId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();

        // 본인이거나
        if (currentUserId.equals(targetUserId)) {
            return true;
        }

        // 관리자 권한이면 허용
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isMessageAuthor(UUID messageId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID currentUserId = userDetails.getUser().getId();

        return messageRepository.findById(messageId)
                .map(message -> message.getAuthor().getId().equals(currentUserId))
                .orElse(false);
    }

    public boolean isMessageAuthorOrAdmin(UUID messageId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID currentUserId = userDetails.getUser().getId();

        return messageRepository.findById(messageId)
                .map(message ->
                        message.getAuthor().getId().equals(currentUserId) ||
                                authentication.getAuthorities().stream()
                                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
                )
                .orElse(false);
    }

    public boolean isSelf(UUID userId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser().getId().equals(userId);
    }


    public boolean isReadStatusOwner(UUID readStatusId, Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            UUID currentUserId = userDetails.getUser().getId();

            return readStatusRepository.findById(readStatusId)
                    .map(rs -> rs.getUser().getId().equals(currentUserId))
                    .orElse(false);
        }
        return false;
    }
}

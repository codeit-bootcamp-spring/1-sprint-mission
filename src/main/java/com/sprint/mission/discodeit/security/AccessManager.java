package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.user.UserDto;
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

    public boolean isSelf(UUID userId, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDto userDto) {
            return userDto.getId().equals(userId);
        }
        return false;
    }

    public boolean isReadStatusOwner(UUID readStatusId, Authentication authentication) {
        UUID currentUserId = extractUserId(authentication);
        if (currentUserId == null) return false;

        return readStatusRepository.findById(readStatusId)
                .map(rs -> rs.getUser().getId().equals(currentUserId))
                .orElse(false);
    }

    public boolean isSelfOrAdmin(UUID targetUserId, Authentication authentication) {
        UUID currentUserId = extractUserId(authentication);
        if (currentUserId == null) return false;

        if (currentUserId.equals(targetUserId)) {
            return true;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isMessageAuthor(UUID messageId, Authentication authentication) {
        UUID currentUserId = extractUserId(authentication);
        if (currentUserId == null) return false;

        return messageRepository.findById(messageId)
                .map(msg -> msg.getAuthor().getId().equals(currentUserId))
                .orElse(false);
    }

    public boolean isMessageAuthorOrAdmin(UUID messageId, Authentication authentication) {
        UUID currentUserId = extractUserId(authentication);
        if (currentUserId == null) return false;

        return messageRepository.findById(messageId)
                .map(msg ->
                        msg.getAuthor().getId().equals(currentUserId) ||
                                authentication.getAuthorities().stream()
                                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
                )
                .orElse(false);
    }

    private UUID extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDto userDto) {
            return userDto.getId();
        }
        return null;
    }
}

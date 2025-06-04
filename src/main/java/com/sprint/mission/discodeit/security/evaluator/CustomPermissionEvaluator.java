package com.sprint.mission.discodeit.security.evaluator;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import java.io.Serializable;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject,
        Object permission) {
        if (authentication == null || targetDomainObject == null) {
            return false;
        }

        if (targetDomainObject instanceof Message message) {
            return checkMessagePermission(authentication, message, permission.toString());
        }

        if (targetDomainObject instanceof ReadStatus readStatus) {
            return checkReadStatusPermission(authentication, readStatus, permission.toString());
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
        String targetType, Object permission) {
        if (authentication == null || targetId == null || targetType == null) {
            return false;
        }

        if (targetType.equals("Message") && targetId instanceof UUID id) {
            if (permission.equals("UPDATE") || permission.equals("DELETE")) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                return messageRepository.existsByIdAndAuthorId(id, userDetails.getId());
            }
        }

        if (targetType.equals("ReadStatus") && targetId instanceof UUID id) {
            if (permission.equals("CREATE") || permission.equals("UPDATE")) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                return readStatusRepository.existsByIdAndUserId(id, userDetails.getId());
            }
        }

        return false;
    }

    private boolean checkMessagePermission(Authentication authentication, Message message,
        String permission) {

        if (permission.equals("UPDATE") || permission.equals("DELETE")) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return message.getAuthor().getId().equals(userDetails.getId());
        }

        return false;
    }

    private boolean checkReadStatusPermission(Authentication authentication, ReadStatus readStatus,
        String permission) {

        if (permission.equals("UPDATE") || permission.equals("DELETE")) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return readStatus.getUser().getId().equals(userDetails.getId());
        }

        return false;
    }
}

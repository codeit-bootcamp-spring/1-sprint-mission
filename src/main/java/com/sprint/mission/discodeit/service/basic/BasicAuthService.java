package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.global.exception.BusinessException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;
    private final ApplicationEventPublisher eventPublisher;

    // 사용자 권한 변경하기
    @Override
    @Transactional
    public UserResponse changeUserRole(UserRoleUpdateRequest request) {
        UUID targetUserId = request.getUserId();
        Role newRole = request.getNewRole();

        User targetUser = userRepository.findById(targetUserId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND,
                Map.of("userId", targetUserId)));

        // 권한 변경
        Role oldRole = targetUser.getRole();

        if (newRole == oldRole) {
            log.info("변경 전과 동일한 권한입니다. (userId: {}, role: {})", targetUserId, oldRole);
            return userMapper.entityToDto(targetUser);
        }

        targetUser.updateRole(newRole);
        userRepository.save(targetUser);

        forceLogoutUser(targetUserId);

        log.info("사용자 권한 변경: {} -> {} (userId: {})", oldRole, newRole, targetUserId);

        NotificationEvent event = NotificationEvent.builder()
            .receiverId(targetUserId)
            .type(NotificationType.ROLE_CHANGED)
            .targetId(targetUserId)
            .title("유저 권한이 변경되었습니다.")
            .content(oldRole + "에서 " + newRole + "(으)로 권한이 변경되었습니다.")
            .build();
        eventPublisher.publishEvent(event);

        return userMapper.entityToDto(targetUser);
    }

    private void forceLogoutUser(UUID targetUserId) {

        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof CustomUserDetails userDetails &&
                userDetails.getUserResponse().id().equals(targetUserId)) {

                for (SessionInformation session : sessionRegistry.getAllSessions(principal,
                    false)) {
                    session.expireNow();
                    log.info("사용자 권한 변경으로 세션이 강제 만료되었습니다. (sessionId: {}, userId: {})",
                        session.getSessionId(), targetUserId);
                }
            }
        }
    }

}

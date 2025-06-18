package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.async.notification.NotificationCreateEvent;
import com.spirnt.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.NotificationType;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.security.jwt.JwtSession;
import com.spirnt.mission.discodeit.security.jwt.JwtSessionRepository;
import com.spirnt.mission.discodeit.service.AuthService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtSessionRepository jwtSessionRepository;
    private final ApplicationEventPublisher eventPublisher; // 스프링 이벤트 발행

    private UserDto toDto(User user) {
        boolean isUserOnline = jwtSessionRepository.existsByUserIdAndExpirationTimeAfter(
            user.getId(),
            Instant.now());
        return userMapper.toDto(user, isUserOnline);
    }

    @Override
    public String getMe(String refreshToken) {
        JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new IllegalArgumentException("JwtSession Not Found For This User"));
        return jwtSession.getAccessToken();
    }

    @Transactional
    @Override
    public UserDto updateRole(UserRoleUpdateRequest userRoleUpdateRequest) {
        UUID userId = userRoleUpdateRequest.userId();
        User user = userRepository.findById(userRoleUpdateRequest.userId())
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        String title = "권한 변경";
        String content =
            "권한이 " + user.getRole().getRoleName() + "에서 " + userRoleUpdateRequest.newRole()
                .getRoleName() + "(으)로 변경됐습니다.";

        user.updateRole(userRoleUpdateRequest.newRole());

        // 알림 생성 이벤트 발행
        eventPublisher.publishEvent(new NotificationCreateEvent(List.of(user),
            title,
            content,
            NotificationType.ROLE_CHANGED,
            user.getId()));

        return toDto(user);
    }
}

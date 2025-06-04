package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.security.jwt.JwtSession;
import com.spirnt.mission.discodeit.security.jwt.JwtSessionRepository;
import com.spirnt.mission.discodeit.service.AuthService;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final JwtSessionRepository jwtSessionRepository;


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

        user.updateRole(userRoleUpdateRequest.newRole());

        return toDto(user);
    }
}

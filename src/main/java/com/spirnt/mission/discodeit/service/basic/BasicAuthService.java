package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.security.CustomUserDetails;
import com.spirnt.mission.discodeit.service.AuthService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final SessionRegistry sessionRegistry;

    private boolean isUserOnline(String username) {
        return sessionRegistry.getAllPrincipals().stream()
            .filter(principal -> principal instanceof UserDetails)
            .map(principal -> (UserDetails) principal)
            .anyMatch(userDetails -> userDetails.getUsername().equals(username));
    }

    private UserDto toDto(User user) {
        return userMapper.toDto(user, isUserOnline(user.getUsername()));
    }

    @Override
    public UserDto getMe(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return toDto(user);
    }

    @Transactional
    @Override
    public UserDto updateRole(UserRoleUpdateRequest userRoleUpdateRequest) {
        UUID userId = userRoleUpdateRequest.userId();
        User user = userRepository.findById(userRoleUpdateRequest.userId())
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        user.updateRole(userRoleUpdateRequest.newRole());

        // 유저가 로그인 중이라면 세션 무효화
        expireUserSessions(user);

        return toDto(user);
    }

    private void expireUserSessions(User user) {
        List<Object> principals = sessionRegistry.getAllPrincipals();

        for (Object principal : principals) {
            if (principal instanceof CustomUserDetails customUserDetails) {
                if (customUserDetails.getUser().equals(user)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(
                        customUserDetails, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow(); // 세션 무효화
                    }
                }
            }
        }
    }
}

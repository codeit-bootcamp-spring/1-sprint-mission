package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.validator.UserValidator;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;

    @Override
    public UserDto getUserInfo(CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return userMapper.toUserDto(user, getOnline(user));
    }

    @Override
    @Transactional
    public UserDto updateUserRole(UserRoleUpdateRequest request) {
        User user = userValidator.validateUserExistsByUserId(request.userId());
        user.updateRole(request.newRole());

        if (getOnline(user)) {
            // 강제 로그아웃: 현재 세션 제거
            List<Object> principals = sessionRegistry.getAllPrincipals();
            for (Object principal : principals) {
                if (principal instanceof UserDetails u && u.getUsername().equals(user.getName())) {
                    sessionRegistry.getAllSessions(principal, false)
                        .forEach(SessionInformation::expireNow);
                }
            }
        }

        return userMapper.toUserDto(user, false);
    }

    private boolean getOnline(User user) {
        boolean online = false;
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (Objects.equals(((UserDetails) principal).getUsername(), user.getName())) {
                online = true;
            }
        }

        return online;
    }
}

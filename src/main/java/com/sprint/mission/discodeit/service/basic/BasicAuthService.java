package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.util.LoginStatusChecker;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final LoginStatusChecker loginStatusChecker;
    private final JwtSessionRepository jwtSessionRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserDto updateUserRole(UserRoleUpdateRequest request) {
        User user = userValidator.validateUserExistsByUserId(request.userId());
        user.updateRole(request.newRole());

        if (loginStatusChecker.getOnline(user)) {
            JwtSession jwtSession = jwtSessionRepository.findByUserId(user.getId())
                .get(); // 이미 getOnline에서 체크
            jwtService.invalidateRefreshToken(jwtSession.getRefreshToken());
        }

        return userMapper.toUserDto(user, false);
    }
}

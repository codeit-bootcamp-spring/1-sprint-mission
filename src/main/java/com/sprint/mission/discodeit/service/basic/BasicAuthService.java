package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.dto.user.response.LoginResponse;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserValidator userValidator;
    private final UserStatusService userStatusService;
    private final UserMapper userMapper;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userValidator.validateUserExistsByNameAndPassword(loginRequest.name(), loginRequest.password());

        // UserStatus 로그인 여부 변경
        UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest =
                new UpdateUserStatusByUserIdRequest(user.getId(), true);
        userStatusService.updateUserStatusByUserId(updateUserStatusByUserIdRequest);

        return userMapper.toLoginResponse(user);
    }
}

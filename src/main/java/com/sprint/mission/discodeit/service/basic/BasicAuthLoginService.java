package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.login.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.AuthLoginService;
import com.sprint.mission.discodeit.validation.AuthLoginValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthLoginService implements AuthLoginService {
    private final AuthLoginValidator authLoginValidator;
    private final UserMapper userMapper;

    @Override
    public UserDto login(AuthLoginRequest request) {
        String username = request.username();
        String password = request.password();

        // 사용자 조회
        User user = authLoginValidator.findUserByUsername(username);

        // 비밀번호 일치 확인
        authLoginValidator.validatePassword(user, password);

        // 사용자 상태 조회
        UserStatus userStatus = authLoginValidator.findUserStatusByUserId(user.getId());
        boolean isOnline = userStatus.isOnline();
        // UserDto 변환 후 반환
        return userMapper.userEntityToDto(user, isOnline);
    }
}

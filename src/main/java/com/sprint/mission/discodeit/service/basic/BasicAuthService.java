package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserDTO.response login(AuthDTO.loginReq loginDTO) {
        if (userRepository.confirmLogin(loginDTO.username(), loginDTO.password())) {
            // 유저가 존재하는지 찾기
            Map.Entry<Long, User> user = userRepository.findUserByUserName(loginDTO.username());
            Map.Entry<Long, UserStatus> userStatus = userStatusRepository.findUserStatusByUserId(user.getValue().getId());

            // 로그인 했으니 시간 업데이트
            userStatus.getValue().updateAccessedAt();
            userStatusRepository.update(userStatus.getKey(), userStatus.getValue());

            return UserDTO.response.from(user, userStatus.getValue().isOnline());
        }
        throw new CustomException(ErrorCode.LOGIN_FAILED);
    }
}

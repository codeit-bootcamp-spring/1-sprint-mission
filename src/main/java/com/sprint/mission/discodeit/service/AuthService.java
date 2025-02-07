package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDTO;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public UserDTO login(LoginRequest request) {
        User findUser = userRepository.findByName(request.userName()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));

        if (!findUser.getPassword().equals(request.password())) { // 비밀번호가 일치하지 않으면 에러 발생
            throw new ServiceException(ErrorCode.PASSWORD_MISMATCH);
        }
        return UserDTO.fromDomain(findUser); // 비밀번호를 보여주지 않기 위해서 DTO 사용
    }
}

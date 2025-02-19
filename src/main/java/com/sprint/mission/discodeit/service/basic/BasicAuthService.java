package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.login.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDTO;
import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BasicAuthService {
    private final UserRepository userRepository;

    private final UserStatusService userStatusService;

    public UserDTO login(LoginRequest request) { // 요청으로 name과 password가 들어온 상황
        User findUser = userRepository.findById(request.userID()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));

        if (!findUser.getPassword().equals(request.password())) { // 비밀번호가 일치하지 않으면 에러 발생
            throw new ServiceException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (!findUser.getName().equals(request.userName())) {
            throw new ServiceException(ErrorCode.USERNAME_MISMATCH);
        }

        // 로그인을 하면 UserStatus 업데이트하기
        UserStatus userStatus = userStatusService.findByUserId(request.userID()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        UpdateUserStatusRequest updateRequest = new UpdateUserStatusRequest(userStatus.getId(), Instant.now());
        userStatusService.update(updateRequest);

        return UserDTO.fromDomain(findUser); // 비밀번호를 보여주지 않기 위해서 DTO 사용
    }
}

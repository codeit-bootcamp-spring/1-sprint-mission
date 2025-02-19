package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(BasicAuthService.class);

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;


    @Override
    public UserResponse login(LoginRequest request) {
        if (request.email() == null || request.email().trim().isEmpty()) {
            logger.warn("로그인 실패: 이메일이 입력되지 않았음");
            throw new IllegalArgumentException("이메일을 입력하세요.");
        }

        if (request.password() == null || request.password().trim().isEmpty()) {
            logger.warn("로그인 실패: 비밀번호가 입력되지 않았음");
            throw new IllegalArgumentException("비밀번호를 입력하세요.");
        }

        User user = userRepository.findByUsername(request.email());
        if (user == null) {
            logger.warn("로그인 실패: 존재하지 않는 이메일");
            throw new IllegalArgumentException("가입되지 않은 이메일입니다: " + request.email());
        }

        if (!user.getPassword().equals(request.password())) {
            logger.warn("로그인 실패: 잘못된 비밀번호 입력");
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
        if (userStatus == null) {
            userStatus = new UserStatus(user.getId());
        }
        userStatus.updateLastActivityAt(java.time.Instant.now());
        userStatusRepository.save(userStatus);

        return UserResponse.from(user, userStatus);
    }
}
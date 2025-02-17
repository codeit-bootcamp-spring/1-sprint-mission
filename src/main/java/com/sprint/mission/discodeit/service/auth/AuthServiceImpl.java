package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.authService.LoginRequest;
import com.sprint.mission.discodeit.dto.authService.LoginResponse;
import com.sprint.mission.discodeit.dto.userService.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new NoSuchElementException("Invalid email or password"));

        // 비밀번호 검증
        if (!user.getPassword().equals(loginRequest.password())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        //  임시 토큰 생성
        String token = UUID.randomUUID().toString();

        // 로그인 응답 반환
        return new LoginResponse(user.getId(), user.getUsername(), user.getEmail(), token);
    }

}

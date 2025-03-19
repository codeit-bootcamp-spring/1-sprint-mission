package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    // 로그인
    @Override
    public UserDto login(LoginRequestDto loginRequestDto) {
        return checkAccount(loginRequestDto.name(), loginRequestDto.password());
    }

    // 계정 확인
    private UserDto checkAccount(String username, String password) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        if (user.getPassword().toString().equals(password)) {
            return UserMapper.INSTANCE.toDto(user);
        } else {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
    }
}

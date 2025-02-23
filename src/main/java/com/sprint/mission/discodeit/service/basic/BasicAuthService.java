package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.authDto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.userDto.FindUserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public FindUserResponseDto login(LoginRequestDto loginRequestDto) {
        return checkAccount(loginRequestDto.name(), loginRequestDto.password());
    }

    private FindUserResponseDto checkAccount(String name, String password) {
        User user = userRepository.load().values().stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        if (user.getPassword().toString().equals(password)) {
            return FindUserResponseDto.fromEntity(user);
        } else {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
    }
}

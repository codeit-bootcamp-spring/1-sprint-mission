package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByName(loginDto.name());
        if (user == null || !user.isSamePassword(loginDto.password())) {
            throw new NoSuchElementException("[ERROR] 잘못된 정보입니다.");
        }

        return userService.getUserInfo(user);
    }
}

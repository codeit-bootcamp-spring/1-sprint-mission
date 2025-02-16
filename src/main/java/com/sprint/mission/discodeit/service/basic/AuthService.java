package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User login(LoginDto loginDto) {
        return userRepository.loadAllUser().stream()
                .filter(user -> user.getUsername().equals(loginDto.username())
                        && user.getPassword().equals(loginDto.password()))
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException("알 수 없는 사용자입니다."));
    }
}
package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(LoginRequest loginRequest) {
        User user = userRepository.findById(loginRequest.userId())
                .orElseThrow(() -> new NoSuchElementException("사용자가 없습니다."));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
        return user;
    }
}

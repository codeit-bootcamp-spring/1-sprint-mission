package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public boolean login(String email, String password) {
        List<User> users = userRepository.readAll();

        boolean login = users.stream()
                        .anyMatch(user -> user.getEmail().equals(email) && user.getPassword().equals(password));

        if (!login) System.out.println("로그인정보가 틀립니다.");
        else System.out.println("로그인이 완료되었습니다.");

        return login;
    }
}

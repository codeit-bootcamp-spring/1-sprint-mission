package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public boolean login(String username, String password) {
        log.debug("로그인 시도 - username: {}", username);
        List<User> users = userRepository.findAll();

        boolean login = users.stream()
                        .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));

        if (!login) log.warn("로그인 실패 - username: {}", username);
        else log.info("로그인 성공 - username: {}", username);;

        return login;
    }
}

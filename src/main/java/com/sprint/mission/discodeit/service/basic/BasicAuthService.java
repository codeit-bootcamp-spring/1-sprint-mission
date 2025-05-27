package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserExceptions;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public UserDto login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        log.info("Processing user login: username={}", username);

        try {
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Login failed: user not found - username={}", username);
                    return UserExceptions.notFound(username);
                });

            if (!passwordEncoder.matches(password, user.getPassword())) {
                log.warn("Login failed: wrong password - username={}", username);
                throw UserExceptions.invalidPassword(username);
            }

            log.info("Login successful: userId={}, username={}", user.getId(), username);
            return userMapper.toDto(user);
        } catch (Exception e) {
            log.error("Error occurred during login process: username={}, error={}", username,
                e.getMessage(), e);
            throw e;
        }
    }
}

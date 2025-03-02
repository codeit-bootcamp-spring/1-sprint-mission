package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public UserResponse login(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NoSuchElementException("User with email " + email + " not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password");
        }

        UserStatus status = new UserStatus(user.getId(), Instant.now());
        return UserResponse.from(user, status);
    }
}
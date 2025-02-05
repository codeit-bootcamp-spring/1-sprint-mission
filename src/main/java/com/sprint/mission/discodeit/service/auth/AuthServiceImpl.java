package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.AuthService.LoginRequest;
import com.sprint.mission.discodeit.dto.userService.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserDTO login(LoginRequest loginRequest) {
        User user = userRepository.findByusername(loginRequest.username())
                .orElseThrow(() -> new NoSuchElementException("iNVALID USERNAME OR PASSWORD"));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), true);
    }

}

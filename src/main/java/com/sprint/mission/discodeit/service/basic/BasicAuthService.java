package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
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
    public UserDto login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new NoSuchElementException("User with username " + username + " not found"));
        System.out.println("findByUsername  = " + user);
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password");
        }
        //UserLoginRequest
        return new UserDto(user);
    }
}

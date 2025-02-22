package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
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
    public User login(LoginRequest request) {
        User user = userRepository.getUserByEmail(request.email())
                .orElseThrow(()->new NoSuchElementException("User not found"));
        if (!user.getPassword().equals(request.password())){
            throw new IllegalArgumentException("Wrong password");
        }
        return user;
    }
}

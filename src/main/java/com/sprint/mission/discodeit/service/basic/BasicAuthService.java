package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(AuthRequestDto request) {
        return userRepository.getAllUsers().stream()
                .filter(user->
                        Objects.equals(user.getName(), request.getName()) &&
                                Objects.equals(user.getPassword(), request.getPassword()))
                .findFirst()
                .orElseThrow(()->new NoSuchElementException("invalid username or password"));
    }
}

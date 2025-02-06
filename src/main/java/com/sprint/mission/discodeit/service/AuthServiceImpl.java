package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.AuthRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(AuthRequestDTO request) {
        return userRepository.getAllUsers().stream()
                .filter(user->user.getName().equals(request.getName())&&user.getPassword().equals(request.getPassword()))
                .findFirst()
                .orElseThrow(()->new NoSuchElementException("invalid username or password"));
    }
}

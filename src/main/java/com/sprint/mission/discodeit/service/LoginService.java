package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    public User login(String loginId, String password) {
        Optional<User> findUserOptional = userRepository.findByloginId(loginId);
        return findUserOptional.filter(m->m.getPassword().equals(password))
                .orElseThrow();
    }
}

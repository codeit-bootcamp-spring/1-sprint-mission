package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;

    public UserDto login(String username, String password) {
        List<User> users = repository.findAll();
        for (User user : users) {
            if(user.getUserName().equals(username) && user.getHashedPassword()== Objects.hashCode(password)) {
                return new UserDto(user);
            }
        }
        throw new IllegalAccessError("인증 실패!");
    }
}

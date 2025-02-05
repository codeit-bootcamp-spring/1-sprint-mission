package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    UserRepository repository;

    public UserDto authenticate(String username, String password) {
        List<User> users = repository.findAll();
        for (User user : users) {
            if(user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return new UserDto(user);
            }
        }
        throw new IllegalAccessError("인증 실패!");
    }
}

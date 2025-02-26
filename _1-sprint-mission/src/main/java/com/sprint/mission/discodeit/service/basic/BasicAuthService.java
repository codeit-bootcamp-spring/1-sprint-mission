package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.login.LoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public User login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.username()).orElseThrow(()->new NoSuchElementException("user not found"));
        if(user != null && user.getPassword().equals(loginDto.password())){
            return user;
        }else{
            throw new NoSuchElementException("Username or password is incorrect");
        }
    }
}

package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthUserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.LoginFailedException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    public BasicAuthService (UserRepository userRepository) {
        this.userRepository=userRepository;
    }
    @Override
    public User isUserExist(AuthUserDTO authUserDTO) {

        return userRepository.load().values()
                .stream()
                .filter(user -> authUserDTO.name().equals(user.getUserName())
                        && authUserDTO.password().equals(user.getPassword()))
                .findFirst()
                .orElseThrow(()-> new LoginFailedException("로그인 실패"));

    }



}

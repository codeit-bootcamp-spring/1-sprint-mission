package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.AuthUserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

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
                .orElseThrow(()-> new NoSuchElementException("로그인 실패"));

    }



}

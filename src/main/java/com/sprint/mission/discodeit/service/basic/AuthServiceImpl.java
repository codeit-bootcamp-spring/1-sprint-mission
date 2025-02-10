package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.auth.AuthServiceLoginDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;


@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(AuthServiceLoginDTO dto){
    return userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(dto.getUsername())
                        && user.getPassword().equals(dto
                        .getPassword())).findFirst().orElseThrow(() -> new NoSuchElementException("로그인 정보 불일치"));
    }
}

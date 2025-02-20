package com.sprint.mission.discodeit.service.featureBasicService;

import com.sprint.mission.discodeit.dto.auth.LoginDTO;
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
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.userName();
        String password = loginDTO.password();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(username + "해당 닉네임을 가진 유저가 존재하지 않습니다."));
        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다!!");
        }
        return user;
    }
}

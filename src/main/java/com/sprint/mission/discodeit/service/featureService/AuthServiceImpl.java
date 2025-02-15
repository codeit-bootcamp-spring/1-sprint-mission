package com.sprint.mission.discodeit.service.featureService;

import com.sprint.mission.discodeit.dto.auth.LoginDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    @Override
    public User login(LoginDTO loginDTO) {
        Map<UUID, User> userMap = userRepository.findAll();
        User user = userMap.values().stream().filter(value -> value.getUserName().equals(loginDTO.userName())
                && value.getPassword().equals(loginDTO.password())).findAny().orElseThrow(()-> new NoSuchElementException("User를 찾을 수 없습니다."));
        return user;
    }
}

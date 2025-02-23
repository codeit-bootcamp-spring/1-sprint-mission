package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.web.dto.CheckUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public CheckUserDto login(String loginId, String password) {
        Optional<User> findUserOptional = userRepository.findByloginId(loginId);
        User user = findUserOptional.filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 비밀번호 입니다."));
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElse(new UserStatus(user.getId()));
        Boolean online = userStatus.isOnline();
        return new CheckUserDto(user, online);
    }
}

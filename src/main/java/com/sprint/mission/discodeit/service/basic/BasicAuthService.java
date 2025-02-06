package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserDto login(AuthLoginRequest request) {
        // 유저 찾기
        Optional<User> user = userRepository.findByUsername(request.username());
        if (user.isEmpty() || !user.get().getPassword().equals(request.password())) {
            throw new IllegalArgumentException("올바르지 않은 아이디나 비밀번호입니다.");
        }
        UserStatus userStatus = userStatusRepository.findByUserId(user.get().getId());
        userStatus.update(Instant.now());
        userStatusRepository.save(userStatus);
        boolean isOnline = userStatus.isOnline();
        // UserDto 변환 후 반환
        return new UserDto(user.get().getId(), user.get().getUsername(), user.get().getEmail(), user.get().getPhoneNumber(), isOnline);
    }
}

package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.auth.LoginRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.AuthService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final UserStatusService userStatusService;

    @Override
    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByName(loginRequest.name())
                .orElseThrow(() -> new NoSuchElementException("A User with Name: " + loginRequest.name() + " Not Found"));
        if (!user.getPassword().equals(loginRequest.password())) {
            throw new NoSuchElementException("Invalid password");
        }
        // UserStatus Online으로 업데이트
        userStatusService.updateByUserId(user.getId(), new UserStatusUpdate(UserStatusType.ONLINE), Instant.now());
        return user;
    }
}

package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthLoginService;
import com.sprint.mission.discodeit.validation.ValidateAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthLoginService implements AuthLoginService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final ValidateAuth validateAuth;

    @Override
    public UserDto login(AuthLoginRequest request) {
        validateAuth.validateLogin(request.username(), request.password());

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Wrong password");
        }
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User Status not found."));
        boolean isOnline = userStatus.isOnline();
        // UserDto 변환 후 반환
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getProfileId(), isOnline, user.getCreatedAt(), user.getUpdatedAt());
    }
}

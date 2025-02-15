package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }
        return new UserStatus(userId);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return null;
    }

    @Override
    public OnlineStatus getOnlineStatus(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        return userStatus.calculateOnlineStatus();
    }
}
